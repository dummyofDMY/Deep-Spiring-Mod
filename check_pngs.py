import os
import sys
from PIL import Image

def is_valid_png(file_path):
    try:
        with open(file_path, 'rb') as f:
            header = f.read(8)
            # PNG magic bytes
            if header != b'\x89PNG\r\n\x1a\n':
                return False, "Invalid PNG magic bytes"
        # Try to open with PIL
        with Image.open(file_path) as img:
            img.verify()  # Verify the image data
            mode = img.mode
            return True, mode
    except Exception as e:
        return False, str(e)

def check_pngs(directory):
    invalid_pngs = []
    all_pngs = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.lower().endswith('.png'):
                file_path = os.path.join(root, file)
                all_pngs.append(file_path)
                valid, info = is_valid_png(file_path)
                if not valid:
                    invalid_pngs.append((file_path, info))
                else:
                    print(f"{file_path}: mode {info}")
    return all_pngs, invalid_pngs

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python check_pngs.py <directory>")
        sys.exit(1)
    directory = sys.argv[1]
    all_pngs, invalid_pngs = check_pngs(directory)
    print(f"Total PNG files: {len(all_pngs)}")
    if invalid_pngs:
        print("Invalid PNG files:")
        for file_path, error in invalid_pngs:
            print(f"  {file_path}: {error}")
    else:
        print("All PNG files appear valid.")
