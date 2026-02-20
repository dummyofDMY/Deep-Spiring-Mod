from PIL import Image, ImageDraw
import argparse
import os

def resize_with_padding(input_path, target_size, output_path=None, bg_color=(255, 255, 255, 0)):
    """
    调整图片分辨率，保持原始宽高比，不足部分用透明边填充
    
    Args:
        input_path: 输入图片路径
        target_size: 目标尺寸，格式为 (width, height)
        output_path: 输出图片路径
        bg_color: 背景颜色 (R, G, B, A)，默认透明
    """
    # 打开图片并转换为RGBA模式
    img = Image.open(input_path)
    
    # 如果原图不是RGBA模式，转换为RGBA
    if img.mode != 'RGBA':
        img = img.convert('RGBA')
    
    original_width, original_height = img.size
    target_width, target_height = target_size
    
    # 计算缩放比例
    width_ratio = target_width / original_width
    height_ratio = target_height / original_height
    
    # 使用较小的比例确保图片完全在目标区域内
    scale_ratio = min(width_ratio, height_ratio)
    
    # 计算缩放后的尺寸
    new_width = int(original_width * scale_ratio)
    new_height = int(original_height * scale_ratio)
    
    # 缩放图片
    img_resized = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
    
    # 创建新的画布
    new_img = Image.new('RGBA', (target_width, target_height), bg_color)
    
    # 计算居中位置
    x_offset = (target_width - new_width) // 2
    y_offset = (target_height - new_height) // 2
    
    # 将缩放后的图片粘贴到画布中心
    new_img.paste(img_resized, (x_offset, y_offset), img_resized if img.mode == 'RGBA' else None)
    
    # 设置输出路径
    if output_path is None:
        name, ext = os.path.splitext(input_path)
        output_path = f"{name}_{target_width}x{target_height}_padded.png"
    
    # 保存图片
    new_img.save(output_path, "PNG")
    print(f"处理完成：{output_path} (原始: {original_width}x{original_height}, 目标: {target_width}x{target_height})")
    
    return new_img

def batch_resize_with_padding(folder_path, target_size, bg_color=(255, 255, 255, 0)):
    """
    批量处理文件夹中的所有图片
    
    Args:
        folder_path: 文件夹路径
        target_size: 目标尺寸，格式为 (width, height)
        bg_color: 背景颜色
    """
    # 支持的图片格式
    supported_formats = ('.png', '.jpg', '.jpeg', '.bmp', '.gif', '.webp', '.tiff')
    
    for filename in os.listdir(folder_path):
        if filename.lower().endswith(supported_formats):
            input_path = os.path.join(folder_path, filename)
            name, ext = os.path.splitext(filename)
            output_path = os.path.join(folder_path, f"{name}_{target_size[0]}x{target_size[1]}_padded.png")
            
            try:
                resize_with_padding(input_path, target_size, output_path, bg_color)
                print(f"已处理: {filename}")
            except Exception as e:
                print(f"处理失败 {filename}: {e}")

def resize_to_specific_mode(input_path, target_width=None, target_height=None, mode='fit', 
                           output_path=None, bg_color=(255, 255, 255, 0)):
    """
    按特定模式调整图片大小
    
    Args:
        input_path: 输入图片路径
        target_width: 目标宽度
        target_height: 目标高度
        mode: 调整模式
            'fit' - 保持宽高比，填充透明边（默认）
            'fill' - 保持宽高比，裁剪超出部分
            'stretch' - 拉伸填充，不保持宽高比
        output_path: 输出图片路径
        bg_color: 背景颜色
    """
    if mode not in ['fit', 'fill', 'stretch']:
        raise ValueError("mode必须是'fit', 'fill'或'stretch'")
    
    img = Image.open(input_path)
    
    if img.mode != 'RGBA':
        img = img.convert('RGBA')
    
    original_width, original_height = img.size
    
    # 如果只指定了一个维度，则按比例计算另一个维度
    if target_width and not target_height:
        target_height = int(original_height * (target_width / original_width))
    elif target_height and not target_width:
        target_width = int(original_width * (target_height / original_height))
    
    if mode == 'stretch':
        # 直接拉伸
        img_resized = img.resize((target_width, target_height), Image.Resampling.LANCZOS)
        new_img = img_resized
    else:
        # 计算缩放比例
        width_ratio = target_width / original_width
        height_ratio = target_height / original_height
        
        if mode == 'fit':
            # 使用较小的比例
            scale_ratio = min(width_ratio, height_ratio)
        else:  # mode == 'fill'
            # 使用较大的比例
            scale_ratio = max(width_ratio, height_ratio)
        
        new_width = int(original_width * scale_ratio)
        new_height = int(original_height * scale_ratio)
        
        # 缩放图片
        img_resized = img.resize((new_width, new_height), Image.Resampling.LANCZOS)
        
        if mode == 'fit':
            # 创建新画布并居中放置
            new_img = Image.new('RGBA', (target_width, target_height), bg_color)
            x_offset = (target_width - new_width) // 2
            y_offset = (target_height - new_height) // 2
            new_img.paste(img_resized, (x_offset, y_offset), img_resized)
        else:  # mode == 'fill'
            # 裁剪居中部分
            x_offset = (new_width - target_width) // 2
            y_offset = (new_height - target_height) // 2
            new_img = img_resized.crop((x_offset, y_offset, 
                                       x_offset + target_width, 
                                       y_offset + target_height))
    
    if output_path is None:
        name, ext = os.path.splitext(input_path)
        output_path = f"{name}_{target_width}x{target_height}_{mode}.png"
    
    new_img.save(output_path, "PNG")
    print(f"处理完成：{output_path} (模式: {mode}, 原始: {original_width}x{original_height})")
    
    return new_img

if __name__ == "__main__":
    # parser = argparse.ArgumentParser(description='调整图片分辨率，保持宽高比，填充透明边')
    # parser.add_argument('input', help='输入图片路径或文件夹路径')
    # parser.add_argument('width', type=int, help='目标宽度')
    # parser.add_argument('height', type=int, help='目标高度')
    # parser.add_argument('-o', '--output', help='输出图片路径（单张图片处理时使用）')
    # parser.add_argument('-b', '--batch', action='store_true',
    #                    help='批量处理文件夹中的所有图片')
    # parser.add_argument('--bg-color', nargs=4, type=int, default=[255, 255, 255, 0],
    #                    help='背景颜色 RGBA 值，默认 255 255 255 0（透明）')
    # parser.add_argument('--mode', choices=['fit', 'fill', 'stretch'], default='fit',
    #                    help='调整模式: fit=保持比例填充边, fill=保持比例裁剪, stretch=拉伸填充')
    # parser.add_argument('--preview', action='store_true',
    #                    help='处理完成后预览图片')
    
    # args = parser.parse_args()
    
    # target_size = (args.width, args.height)
    # bg_color = tuple(args.bg_color)
    
    # if args.batch:
    #     # 批量处理模式
    #     batch_resize_with_padding(args.input, target_size, bg_color)
    # elif args.mode != 'fit':
    #     # 使用特定模式处理
    #     resize_to_specific_mode(
    #         args.input, 
    #         args.width, 
    #         args.height, 
    #         args.mode, 
    #         args.output, 
    #         bg_color
    #     )
    # else:
    #     # 单张图片处理模式（默认fit模式）
    #     result = resize_with_padding(args.input, target_size, args.output, bg_color)
        
    #     if args.preview:
    #         result.show()
    
    input_path = r"E:\code_java\DeepSpiring\tools\clip_photo\i.png"
    output_path = r"E:\code_java\DeepSpiring\src\main\resources\DeepSpiringModResources\img\powers\Storytime.png"
    target_size = (32, 32)
    resize_with_padding(input_path, target_size, output_path, (255, 255, 255, 0))