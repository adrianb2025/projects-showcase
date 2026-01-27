import os
import tkinter as tk
from tkinter import filedialog, messagebox
from pydub import AudioSegment, silence

def only_numbers(value):
    return value.isdigit() or value == ""

def pick_input_files():
    files = filedialog.askopenfilenames(
        filetypes=[("Audio Files", "*.wav *.mp3 *.ogg")]
    )
    if files:
        input_files_var.set("; ".join(files))

def pick_output_folder():
    folder = filedialog.askdirectory()
    if folder:
        output_dir_var.set(folder)

def show_help():
    messagebox.showinfo(
        "How to Use",
        "Input Audio Files:\n"
        "Select one or more audio files that contain multiple sounds packed sequentially.\n\n"
        "Output Folder:\n"
        "Choose the directory where the sliced audio files will be saved.\n\n"
        "Filename Prefix:\n"
        "Base name used for exported files. Files are numbered starting at 01.\n"
        "Example: FootstepGrass01.wav, FootstepGrass02.wav\n\n"
        "Number of Sounds:\n"
        "How many individual sounds are contained in each input file. "
        "The audio is split evenly based on this number.\n\n"
        "Normalize:\n"
        "Adjusts each slice to a consistent volume level.\n\n"
        "Trim Silence:\n"
        "Removes leading and trailing silence before slicing.\n\n"
        "Create Subfolder:\n"
        "Creates a subfolder using the filename prefix and places the sliced files inside it.\n\n"
        "Slice Audio:\n"
        "Processes all selected files using the current settings."
    )

def slice_audio():
    files = input_files_var.get().split("; ")
    output_dir = output_dir_var.get()
    count = count_var.get()
    prefix = prefix_var.get().strip()

    if not files or not os.path.isfile(files[0]):
        messagebox.showerror("Error", "Please select at least one valid audio file.")
        return

    if not output_dir:
        messagebox.showerror("Error", "Please select an output folder.")
        return

    if not count.isdigit() or int(count) <= 0:
        messagebox.showerror("Error", "Number of sounds must be a positive integer.")
        return

    if not prefix:
        messagebox.showerror("Error", "Filename prefix cannot be empty.")
        return

    count = int(count)
    digits = max(2, len(str(count)))

    for file in files:
        audio = AudioSegment.from_file(file)

        if trim_var.get():
            chunks = silence.split_on_silence(
                audio,
                min_silence_len=200,
                silence_thresh=audio.dBFS - 16,
                keep_silence=50
            )
            audio = sum(chunks) if chunks else audio

        slice_length = len(audio) / count

        target_dir = output_dir
        if subfolder_var.get():
            target_dir = os.path.join(output_dir, prefix)
            os.makedirs(target_dir, exist_ok=True)

        for i in range(count):
            start = int(i * slice_length)
            end = int((i + 1) * slice_length)
            chunk = audio[start:end]

            if normalize_var.get():
                chunk = chunk.normalize()

            index = i + 1
            filename = f"{prefix}{index:0{digits}d}.wav"
            chunk.export(os.path.join(target_dir, filename), format="wav")

    messagebox.showinfo("Completed", "Audio slicing completed successfully.")

root = tk.Tk()
root.title("Audio Slicer")
root.geometry("600x390")
root.resizable(False, False)

input_files_var = tk.StringVar()
output_dir_var = tk.StringVar()
count_var = tk.StringVar(value="6")
prefix_var = tk.StringVar(value="Sound")

normalize_var = tk.BooleanVar(value=True)
trim_var = tk.BooleanVar(value=False)
subfolder_var = tk.BooleanVar(value=True)

vcmd = root.register(only_numbers)

f1 = tk.Frame(root)
f1.pack(fill="x", padx=10, pady=4)
tk.Label(f1, text="Input Audio Files", width=18, anchor="w").pack(side="left")
tk.Entry(f1, textvariable=input_files_var).pack(side="left", fill="x", expand=True, padx=5)
tk.Button(f1, text="Browse", command=pick_input_files).pack(side="left")

f2 = tk.Frame(root)
f2.pack(fill="x", padx=10, pady=4)
tk.Label(f2, text="Output Folder", width=18, anchor="w").pack(side="left")
tk.Entry(f2, textvariable=output_dir_var).pack(side="left", fill="x", expand=True, padx=5)
tk.Button(f2, text="Browse", command=pick_output_folder).pack(side="left")

f3 = tk.Frame(root)
f3.pack(fill="x", padx=10, pady=4)
tk.Label(f3, text="Filename Prefix", width=18, anchor="w").pack(side="left")
tk.Entry(f3, textvariable=prefix_var).pack(side="left", fill="x", expand=True)

f4 = tk.Frame(root)
f4.pack(fill="x", padx=10, pady=4)
tk.Label(f4, text="Number of Sounds", width=18, anchor="w").pack(side="left")
tk.Entry(
    f4,
    textvariable=count_var,
    validate="key",
    validatecommand=(vcmd, "%P"),
    width=10
).pack(side="left")

f5 = tk.Frame(root)
f5.pack(fill="x", padx=10, pady=8)
tk.Checkbutton(f5, text="Normalize", variable=normalize_var).pack(side="left", padx=5)
tk.Checkbutton(f5, text="Trim Silence", variable=trim_var).pack(side="left", padx=5)
tk.Checkbutton(f5, text="Create Subfolder", variable=subfolder_var).pack(side="left", padx=5)

f6 = tk.Frame(root)
f6.pack(fill="x", padx=10, pady=8)
tk.Button(f6, text="How to Use", command=show_help).pack(side="left")
tk.Button(f6, text="Slice Audio", command=slice_audio, height=2).pack(side="right")

root.mainloop()
