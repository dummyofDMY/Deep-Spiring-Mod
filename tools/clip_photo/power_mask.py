from PIL import Image, ImageDraw
import numpy as np
import math
import os

def semi_ellipse_crop(image_path, output_path, semi_minor_axis, position='bottom'):
    """
    使用半椭圆弧裁剪图片，半椭圆与图片底边和两侧相切
    
    参数:
        image_path: 输入图片路径
        output_path: 输出图片路径
        semi_minor_axis: 半椭圆短轴长度（像素）
        position: 半椭圆位置，'bottom'(默认) 或 'top'
    """
    # 打开图片
    img = Image.open(image_path).convert("RGBA")
    width, height = img.size
    
    # 椭圆参数
    semi_major_axis = width / 2  # 长半轴是宽度的一半
    center_x = width / 2
    semi_minor_axis *= height
    
    if position == 'bottom':
        center_y = height  # 中心在图片下方
    else:  # 'top'
        center_y = 0  # 中心在图片上方
    
    # 转换为numpy数组处理
    data = np.array(img)
    
    # 处理每个像素
    for y in range(height):
        for x in range(width):
            if position == 'bottom':
                # 底部半椭圆：y >= (height - semi_minor_axis) 且在椭圆内
                if y >= height - semi_minor_axis:
                    y_relative = y - (height - semi_minor_axis)
                    # 椭圆方程：(x-center_x)^2/a^2 + (y_relative)^2/b^2 <= 1
                    if ((x - center_x) ** 2) / (semi_major_axis ** 2) + \
                       (y_relative ** 2) / (semi_minor_axis ** 2) >= 1:
                        data[y, x, 3] = 0  # 设置为透明
            else:  # top
                # 顶部半椭圆：y <= semi_minor_axis 且在椭圆内
                if y <= semi_minor_axis:
                    # 椭圆方程：(x-center_x)^2/a^2 + (y)^2/b^2 <= 1
                    if ((x - center_x) ** 2) / (semi_major_axis ** 2) + \
                       (y ** 2) / (semi_minor_axis ** 2) <= 1:
                        data[y, x, 3] = 0  # 设置为透明
    
    # 创建结果图片
    result = Image.fromarray(data, 'RGBA')
    result.save(output_path)
    
    print(f"裁剪完成！短轴={semi_minor_axis}px")
    return result


def create_preview(image_path, semi_minor_axis, position='bottom'):
    """
    创建预览图，显示椭圆轮廓
    """
    img = Image.open(image_path).convert("RGBA")
    width, height = img.size
    
    # 创建预览图
    preview = img.copy()
    draw = ImageDraw.Draw(preview)
    
    # 椭圆参数
    semi_major_axis = width / 2
    center_x = width / 2
    
    # 绘制椭圆点
    points = []
    
    if position == 'bottom':
        for y in range(0, semi_minor_axis + 1):
            x_offset = int(semi_major_axis * math.sqrt(1 - ((y / semi_minor_axis) ** 2)))
            x1 = int(center_x - x_offset)
            x2 = int(center_x + x_offset)
            y_pos = height - y
            points.append((x1, y_pos))
            if x1 != x2:
                points.append((x2, y_pos))
    else:  # top
        for y in range(0, semi_minor_axis + 1):
            x_offset = int(semi_major_axis * math.sqrt(1 - ((y / semi_minor_axis) ** 2)))
            x1 = int(center_x - x_offset)
            x2 = int(center_x + x_offset)
            points.append((x1, y))
            if x1 != x2:
                points.append((x2, y))
    
    # 连接点绘制轮廓
    if len(points) > 1:
        for i in range(0, len(points) - 1, 2):
            if i + 1 < len(points):
                draw.line([points[i], points[i+1]], fill=(255, 0, 0, 200), width=3)
    
    return preview


def main():
    # 在这里设置参数
    input_image = r"E:\code_java\DeepSpiring\tools\clip_photo\input.png"
    output_image = os.path.basename(input_image)
    semi_minor_axis = 0.5            # 椭圆短轴长度（像素）
    position = 'bottom'              # 裁剪位置：'bottom' 或 'top'
    show_preview = False              # 是否显示预览
    
    # 检查文件是否存在
    if not os.path.exists(input_image):
        print(f"错误：图片不存在 {input_image}")
        return
    
    # 获取图片尺寸
    img = Image.open(input_image)
    width, height = img.size
    
    print(f"图片尺寸: {width}×{height}")
    print(f"椭圆短轴: {semi_minor_axis}px")
    print(f"椭圆长轴: {width}px (图片宽度)")
    print(f"裁剪位置: {position}")
    
    # 检查参数有效性
    if semi_minor_axis <= 0 or semi_minor_axis > height:
        print(f"错误：短轴长度必须在1到{height}之间")
        return
    
    # 创建预览
    if show_preview:
        preview = create_preview(input_image, semi_minor_axis, position)
        preview_path = f"preview_{os.path.basename(input_image)}"
        preview.save(preview_path)
        print(f"预览图已保存: {preview_path}")
    
    # 执行裁剪
    result = semi_ellipse_crop(input_image, output_image, semi_minor_axis, position)
    
    print(f"裁剪完成！输出文件: {output_image}")


if __name__ == "__main__":
    main()