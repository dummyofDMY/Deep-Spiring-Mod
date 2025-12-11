import os
import sys
from PIL import Image

def convert_to_rgba(file_path):
    try:
        with Image.open(file_path) as img:
            if img.mode != 'RGBA':
                rgba_img = img.convert('RGBA')
                rgba_img.save(file_path)
                print(f"Converted {file_path} from {img.mode} to RGBA")
            else:
                print(f"{file_path} is already RGBA")
    except Exception as e:
        print(f"Error converting {file_path}: {e}")

def convert_pngs(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.lower().endswith('.png'):
                file_path = os.path.join(root, file)
                convert_to_rgba(file_path)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python convert_pngs.py <directory>")
        sys.exit(1)
    directory = sys.argv[1]
    convert_pngs(directory)
    print("Conversion complete.")
