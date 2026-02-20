import cv2
import numpy as np
import os

def invert_red_blue_RGBA(image_path, output_path=None):
    """
    为RGBA图进行红蓝通道反转，保持透明度不变
    
    参数:
        image_path: RGBA图片路径
        output_path: 输出图片路径，如果为None则自动生成
    """
    # 读取RGBA图（确保读取时包含透明度通道）
    img = cv2.imread(image_path, cv2.IMREAD_UNCHANGED)
    if img is None:
        print(f"无法读取RGBA图片: {image_path}")
        return None
    
    # 检查图片是否是RGBA格式
    if len(img.shape) == 2:  # 灰度图
        print("警告：图片是灰度图，需要转换为RGBA格式")
        # 转换为RGBA
        img = cv2.cvtColor(img, cv2.COLOR_GRAY2RGBA)
    elif len(img.shape) == 3:  # 彩色图
        channels = img.shape[2]
        if channels == 3:  # RGB三通道
            print("RGB图检测到，转换为RGBA格式（透明度设为255）")
            img = cv2.cvtColor(img, cv2.COLOR_RGB2RGBA)
        elif channels == 4:  # RGBA四通道
            # 确认是RGBA而不是BGRA
            # OpenCV默认读取为BGRA，我们需要转换为RGBA
            if cv2.imread(image_path).shape[2] == 3:  # 如果原图是BGR
                img = cv2.cvtColor(img, cv2.COLOR_BGRA2RGBA)
            else:
                # 假设已经是RGBA，但需要确认通道顺序
                pass
        else:
            print(f"不支持{channels}通道的图片")
            return None
    
    # 确保图片是RGBA格式（4通道）
    if img.shape[2] != 4:
        print("错误：图片不是RGBA格式")
        return None
    
    # 分离RGBA通道
    # 现在img应该是RGBA格式，通道顺序为：[R, G, B, A]
    r = img[:, :, 0].copy()
    g = img[:, :, 1].copy()
    b = img[:, :, 2].copy()
    a = img[:, :, 3].copy()
    
    # 反转红色和蓝色通道，保持绿色和透明度不变
    img_inverted = np.stack([b, g, r, a], axis=2)
    
    # 如果未指定输出路径，自动生成
    if output_path is None:
        # 保持与原图相同的扩展名，优先使用PNG保存RGBA
        base_name = os.path.splitext(image_path)[0]
        extension = os.path.splitext(image_path)[1]
        if extension.lower() not in ['.png', '.tiff', '.tif', '.webp']:
            print("RGBA图建议保存为PNG格式以确保透明度支持")
            extension = '.png'
        output_path = f"{base_name}_红蓝反转_RGBA{extension}"
    
    # 保存处理后的RGBA图
    # 转换为BGRA格式保存（OpenCV保存需要）
    bgra_inverted = cv2.cvtColor(img_inverted, cv2.COLOR_RGBA2BGRA)
    cv2.imwrite(output_path, bgra_inverted, [cv2.IMWRITE_PNG_COMPRESSION, 9])
    
    print(f"RGBA图已处理并保存到: {output_path}")
    print(f"输出格式: RGBA (通道顺序: R←→B反转，G不变，A不变)")
    
    return img_inverted

def verify_RGBA_channels(image_path):
    """
    验证图片是否为RGBA格式并显示通道信息
    """
    img = cv2.imread(image_path, cv2.IMREAD_UNCHANGED)
    if img is None:
        print("无法读取图片")
        return
    
    print(f"图片信息:")
    print(f"  尺寸: {img.shape[0]} x {img.shape[1]}")
    print(f"  通道数: {img.shape[2] if len(img.shape) == 3 else 1}")
    
    if len(img.shape) == 3 and img.shape[2] == 4:
        print("  ✓ 确认是RGBA格式图片")
        
        # 显示各通道统计信息
        for i, channel_name in enumerate(['R', 'G', 'B', 'A']):
            channel_data = img[:, :, i]
            print(f"  {channel_name}通道: 最小值={channel_data.min()}, 最大值={channel_data.max()}, "
                  f"平均值={channel_data.mean():.1f}")
        
        # 检查透明度通道特性
        alpha_channel = img[:, :, 3]
        transparent_pixels = np.sum(alpha_channel < 255)
        total_pixels = alpha_channel.size
        transparency_percent = (transparent_pixels / total_pixels) * 100
        print(f"  透明度特性: {transparent_pixels}个像素({transparency_percent:.1f}%)有透明度")
    
    elif len(img.shape) == 3 and img.shape[2] == 3:
        print("  检测到RGB格式，需要转换为RGBA")
    else:
        print("  不是RGBA格式图片")

def batch_process_RGBA(input_folder, output_folder=None):
    """
    批量处理文件夹中的所有RGBA图
    """
    import glob
    
    if not os.path.exists(input_folder):
        print(f"文件夹不存在: {input_folder}")
        return
    
    # 支持的图片格式
    image_extensions = ['.png', '.tiff', '.tif', '.webp', '.jpg', '.jpeg', '.bmp']
    
    # 创建输出文件夹
    if output_folder is None:
        output_folder = os.path.join(input_folder, 'RGBA_红蓝反转')
    
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    
    # 查找所有图片文件
    all_images = []
    for ext in image_extensions:
        pattern = os.path.join(input_folder, f'*{ext}')
        all_images.extend(glob.glob(pattern))
        pattern_upper = os.path.join(input_folder, f'*{ext.upper()}')
        all_images.extend(glob.glob(pattern_upper))
    
    print(f"找到 {len(all_images)} 个图片文件")
    
    processed_count = 0
    for image_path in all_images:
        try:
            print(f"\n处理: {os.path.basename(image_path)}")
            verify_RGBA_channels(image_path)
            
            # 生成输出路径
            image_name = os.path.basename(image_path)
            name_without_ext = os.path.splitext(image_name)[0]
            output_path = os.path.join(output_folder, f"{name_without_ext}_RGBA_红蓝反转.png")
            
            # 处理图片
            result = invert_red_blue_RGBA(image_path, output_path)
            if result is not None:
                processed_count += 1
            
        except Exception as e:
            print(f"处理文件 {image_path} 时出错: {str(e)}")
    
    print(f"\n批量处理完成！成功处理 {processed_count}/{len(all_images)} 个文件")
    print(f"结果保存在: {output_folder}")

def show_RGBA_comparison(image_path):
    """
    显示RGBA图处理前后的对比，正确显示透明度
    """
    try:
        # 处理图片
        result = invert_red_blue_RGBA(image_path)
        
        if result is not None:
            # 读取原图（用于显示）
            original_rgba = cv2.imread(image_path, cv2.IMREAD_UNCHANGED)
            if original_rgba.shape[2] == 3:
                original_rgba = cv2.cvtColor(original_rgba, cv2.COLOR_BGR2RGBA)
            elif original_rgba.shape[2] == 4:
                original_rgba = cv2.cvtColor(original_rgba, cv2.COLOR_BGRA2RGBA)
            
            # 创建白色背景用于正确显示透明度
            import matplotlib.pyplot as plt
            
            fig, axes = plt.subplots(2, 4, figsize=(16, 8))
            
            # 原图
            axes[0, 0].imshow(original_rgba)
            axes[0, 0].set_title("原始RGBA图")
            axes[0, 0].axis('off')
            
            # 原图各通道
            channel_names = ['R通道', 'G通道', 'B通道', 'A通道']
            for i in range(4):
                axes[0, i+1].imshow(original_rgba[:, :, i], cmap='gray' if i == 3 else 'Reds' if i == 0 else 'Greens' if i == 1 else 'Blues')
                axes[0, i+1].set_title(channel_names[i])
                axes[0, i+1].axis('off')
            
            # 处理后
            axes[1, 0].imshow(result)
            axes[1, 0].set_title("红蓝反转后")
            axes[1, 0].axis('off')
            
            # 处理后各通道
            for i in range(4):
                axes[1, i+1].imshow(result[:, :, i], cmap='gray' if i == 3 else 'Reds' if i == 2 else 'Greens' if i == 1 else 'Blues')
                axes[1, i+1].set_title(f"{channel_names[i]}" + (" (反转)" if i in [0, 2] else ""))
                axes[1, i+1].axis('off')
            
            plt.suptitle("RGBA图红蓝通道反转对比分析", fontsize=16)
            plt.tight_layout()
            plt.show()
    
    except Exception as e:
        print(f"显示对比图时出错: {str(e)}")

# 使用示例
if __name__ == "__main__":
    # import argparse
    
    # parser = argparse.ArgumentParser(description='RGBA图红蓝通道反转工具')
    # parser.add_argument('input', default=r"E:\code_java\DeepSpiring\tools\clip_photo\i.webp", type=str, help='RGBA图片路径或文件夹路径')
    # parser.add_argument('-o', '--output', default=r"E:\code_java\DeepSpiring\i.png", type=str, help='输出路径')
    # parser.add_argument('-v', '--verify', action='store_true', help='验证RGBA通道信息')
    # parser.add_argument('-c', '--compare', default=False, action='store_true', help='显示处理前后对比')
    
    # args = parser.parse_args()
    
    # if os.path.isfile(args.input):
    #     # 处理单个文件
    #     if args.verify:
    #         verify_RGBA_channels(args.input)
    #     elif args.compare:
    #         show_RGBA_comparison(args.input)
    #     else:
    #         invert_red_blue_RGBA(args.input, args.output)
    
    # elif os.path.isdir(args.input):
    #     # 处理文件夹
    #     batch_process_RGBA(args.input, args.output)
    
    # else:
    #     print(f"路径不存在: {args.input}")
    
    input = r"E:\code_java\DeepSpiring\tools\clip_photo\i.webp"
    output = r"E:\code_java\DeepSpiring\out.png"
    verify_RGBA_channels(input)
    invert_red_blue_RGBA(input, output)