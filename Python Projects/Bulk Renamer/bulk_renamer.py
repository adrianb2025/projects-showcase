import os
import re
import tkinter as tk
from tkinter import filedialog, messagebox


class BulkRenamer:

    def __init__(self, root):
        self.root = root
        self.root.title("Bulk File Renamer")

        self.folder_path = ""

        tk.Button(root, text="Select Folder", command=self.select_folder).pack(pady=5)

        self.folder_label = tk.Label(root, text="No folder selected")
        self.folder_label.pack()

        tk.Label(root, text="Base Name").pack()
        self.base_name = tk.Entry(root)
        self.base_name.pack()

        tk.Label(root, text="Number Padding (optional)").pack()
        self.padding = tk.Entry(root)
        self.padding.insert(0, "0")
        self.padding.pack()

        tk.Label(root, text="Start Number").pack()
        self.start_number = tk.Entry(root)
        self.start_number.insert(0, "1")
        self.start_number.pack()

        tk.Button(root, text="Preview", command=self.preview).pack(pady=5)

        self.preview_box = tk.Text(root, height=12, width=70, state="disabled")
        self.preview_box.pack()

        tk.Button(root, text="Rename Files", command=self.rename_files).pack(pady=10)

    def select_folder(self):
        self.folder_path = filedialog.askdirectory()
        self.folder_label.config(text=self.folder_path)

    def natural_key(self, s):
        return [
            int(text) if text.isdigit() else text.lower()
            for text in re.split(r'(\d+)', s)
        ]

    def get_files_only(self):
        items = os.listdir(self.folder_path)

        files = []
        for item in items:
            full_path = os.path.join(self.folder_path, item)

            if os.path.isfile(full_path):
                files.append(item)

        files.sort(key=self.natural_key)
        return files

    def preview(self):
        if not self.folder_path:
            return

        files = self.get_files_only()

        base = self.base_name.get()
        padding = int(self.padding.get()) if self.padding.get() else 0
        number = int(self.start_number.get())

        self.preview_box.config(state="normal")
        self.preview_box.delete("1.0", tk.END)

        for file in files:
            name, ext = os.path.splitext(file)

            num = str(number).zfill(padding) if padding > 0 else str(number)
            new_name = f"{base}{num}{ext}"

            self.preview_box.insert(tk.END, f"{file}  →  {new_name}\n")

            number += 1

        self.preview_box.config(state="disabled")

    def rename_files(self):
        if not self.folder_path:
            messagebox.showerror("Error", "No folder selected")
            return

        files = self.get_files_only()

        base = self.base_name.get()
        padding = int(self.padding.get()) if self.padding.get() else 0
        number = int(self.start_number.get())

        for file in files:
            old_path = os.path.join(self.folder_path, file)

            name, ext = os.path.splitext(file)

            num = str(number).zfill(padding) if padding > 0 else str(number)
            new_name = f"{base}{num}{ext}"

            new_path = os.path.join(self.folder_path, new_name)

            os.rename(old_path, new_path)

            number += 1

        messagebox.showinfo("Done", "Files renamed successfully!")


root = tk.Tk()
app = BulkRenamer(root)
root.mainloop()
