from PIL import Image
import numpy as np
import os

def simple_bottom_rounded_corners():
    """简化版：只处理底部透明和圆角"""
    
    # ====== 参数设置 ======
    input_file = r"E:\code_java\DeepSpiring\out.png"
    output_image = os.path.basename(input_file)
    
    radius_ratio = 0.1            # 圆角半径比例 (0-1)
    bottom_transparent_ratio = 0.03  # 小图
    bottom_transparent_ratio = 0.05  # 大图
    # ====================
    
    img = Image.open(input_file).convert("RGBA")
    width, height = img.size
    data = np.array(img)
    
    # 计算实际像素值
    radius = int(width * radius_ratio)
    bottom_height = int(height * bottom_transparent_ratio)
    
    # 1. 先将底部区域设为透明
    if bottom_height > 0:
        data[height - bottom_height:, :, 3] = 0
    
    # 2. 在剩余区域处理圆角
    if radius > 0:
        y_coords, x_coords = np.indices((height, width))
        
        # 左下角圆角
        mask_bl = (x_coords < radius) & (y_coords >= height - radius - bottom_height)
        circle_bl = (x_coords - radius) ** 2 + (y_coords - (height - radius - bottom_height)) ** 2 > radius ** 2
        data[mask_bl & circle_bl, 3] = 0
        
        # 右下角圆角
        mask_br = (x_coords >= width - radius) & (y_coords >= height - radius - bottom_height)
        circle_br = (x_coords - (width - radius)) ** 2 + (y_coords - (height - radius - bottom_height)) ** 2 > radius ** 2
        data[mask_br & circle_br, 3] = 0
    
    # 保存结果
    Image.fromarray(data, 'RGBA').save(output_image)
    
    print(f"处理完成！")
    print(f"圆角半径: {radius}px (宽度×{radius_ratio})")
    print(f"底部透明: {bottom_height}px (高度×{bottom_transparent_ratio})")

if __name__ == "__main__":
    simple_bottom_rounded_corners()