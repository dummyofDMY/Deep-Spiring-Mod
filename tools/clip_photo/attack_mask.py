from PIL import Image
import numpy as np
import os

def add_triangle_transparency(image_path, output_path, base_pixels, height_pixels):
    """
    简洁版本：直接使用像素值指定底边和高
    
    参数:
        image_path: 输入图片路径
        output_path: 输出图片路径
        base_pixels: 三角形底边宽度（像素）
        height_pixels: 三角形高度（像素）
    """
    img = Image.open(image_path).convert("RGBA")
    width, height = img.size
    
    # 限制参数范围
    base_pixels *= width
    height_pixels *= height
    base_pixels = int(base_pixels)
    height_pixels = int(height_pixels)
    
    data = np.array(img)
    
    # 处理左下角三角形
    for y in range(height - height_pixels, height):
        # 计算当前行宽度
        y_ratio = (y - (height - height_pixels)) / height_pixels
        current_width = int(base_pixels * y_ratio)
        
        # 设置透明
        data[y, :current_width, 3] = 0  # 左下角
        data[y, width - current_width:, 3] = 0  # 右下角
    
    Image.fromarray(data, 'RGBA').save(output_path)
    print(f"已添加三角形透明区域: 底边={base_pixels}px, 高度={height_pixels}px")


# 带交互的版本
def interactive_triangle_transparency():
    """交互式设置三角形参数"""
    input_path = input("请输入输入图片路径: ").strip()
    output_path = input("请输入输出图片路径: ").strip()
    
    # 获取图片信息
    img = Image.open(input_path)
    width, height = img.size
    print(f"\n图片尺寸: {width}×{height}")
    
    # 获取三角形参数
    print("\n设置三角形参数:")
    
    # 底边设置
    use_ratio = input("使用比例设置底边？(y/n, 默认n): ").strip().lower() == 'y'
    if use_ratio:
        ratio = float(input(f"请输入底边占图片宽度的比例(0-1, 默认0.25): ") or 0.25)
        base_pixels = int(width * ratio)
    else:
        base_pixels = int(input(f"请输入底边宽度(像素, 最大{width}): ") or (width // 4))
    
    # 高度设置
    use_ratio = input("使用比例设置高度？(y/n, 默认n): ").strip().lower() == 'y'
    if use_ratio:
        ratio = float(input(f"请输入高度占图片高度的比例(0-1, 默认0.25): ") or 0.25)
        height_pixels = int(height * ratio)
    else:
        height_pixels = int(input(f"请输入高度(像素, 最大{height}): ") or (height // 4))
    
    # 选择位置
    position = input("透明区域位置(left/right/both, 默认both): ").strip().lower() or 'both'
    
    # 处理图片
    result = create_triangle_transparency(
        input_path, output_path,
        base_pixels, height_pixels,
        position
    )
    
    print(f"\n处理完成！")
    print(f"输出文件: {output_path}")
    print(f"透明三角形参数:")
    print(f"  - 底边: {base_pixels}像素 ({base_pixels/width*100:.1f}%宽度)")
    print(f"  - 高度: {height_pixels}像素 ({height_pixels/height*100:.1f}%高度)")
    print(f"  - 位置: {position}")
    
    return result


if __name__ == "__main__":
    file = r"E:\code_java\DeepSpiring\tools\clip_photo\input.png"
    file_name = os.path.basename(file)
    # 方法1：直接使用
    add_triangle_transparency(
        file, 
        file_name, 
        base_pixels=0.4,
        height_pixels=0.2
    )
    
    # 方法2：交互模式
    # interactive_triangle_transparency()