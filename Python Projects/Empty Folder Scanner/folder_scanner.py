import os
import tkinter as tk
from tkinter import ttk, filedialog, messagebox
import tkinter.font as tkfont



def find_empty_folders(root_dir):
    empty_folders = []
    for current_path, dirs, files in os.walk(root_dir):
        if not dirs and not files:
            empty_folders.append(current_path)
    return empty_folders


def browse_folder():
    folder_selected = filedialog.askdirectory()
    if folder_selected:
        entry_path.delete(0, tk.END)
        entry_path.insert(0, folder_selected)


def scan_folders():
    root_dir = entry_path.get()

    if not os.path.isdir(root_dir):
        messagebox.showerror("Error", "Invalid directory selected.")
        return

    # Clear table
    for row in tree.get_children():
        tree.delete(row)

    empty_folders = find_empty_folders(root_dir)

    for folder in empty_folders:
        folder_name = os.path.basename(folder)
        tree.insert("", tk.END, values=(folder_name, folder))

    result_label.config(text=f"Found {len(empty_folders)} empty folders.")
    auto_size_column()

def auto_size_column():
    max_width = 100
    for item in tree.get_children():
        text = tree.item(item)["values"][0]
        width = tk.font.Font().measure(text)
        if width > max_width:
            max_width = width

    tree.column("Folder Name", width=max_width + 20)

root = tk.Tk()
root.title("Empty Folder Scanner")
root.geometry("1100x650")
root.minsize(800, 500)

root.columnconfigure(0, weight=1)
root.rowconfigure(2, weight=1)

top_frame = ttk.Frame(root, padding=10)
top_frame.grid(row=0, column=0, sticky="ew")

top_frame.columnconfigure(0, weight=1)

entry_path = ttk.Entry(top_frame)
entry_path.grid(row=0, column=0, sticky="ew", padx=(0, 5))

btn_browse = ttk.Button(top_frame, text="Browse", command=browse_folder)
btn_browse.grid(row=0, column=1)

btn_scan = ttk.Button(root, text="Scan for Empty Folders", command=scan_folders)
btn_scan.grid(row=1, column=0, pady=(0, 5))

table_frame = ttk.Frame(root)
table_frame.grid(row=2, column=0, sticky="nsew", padx=10, pady=(0, 10))

root.rowconfigure(2, weight=1)
root.columnconfigure(0, weight=1)

table_frame.rowconfigure(0, weight=1)
table_frame.columnconfigure(0, weight=1)

columns = ("Folder Name", "Full Path")

tree = ttk.Treeview(
    table_frame,
    columns=columns,
    show="headings"
)

tree.heading("Folder Name", text="Folder Name", anchor="center")
tree.heading("Full Path", text="Full Path", anchor="center")

tree.column("Folder Name", anchor="center", width=150, stretch=False)
tree.column("Full Path", anchor="center", width=800, stretch=True)

tree.grid(row=0, column=0, sticky="nsew")

scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=tree.yview)
scrollbar.grid(row=0, column=1, sticky="ns")

tree.configure(yscrollcommand=scrollbar.set)

result_label = ttk.Label(root, text="")
result_label.grid(row=3, column=0, pady=(0, 10))

root.mainloop()
