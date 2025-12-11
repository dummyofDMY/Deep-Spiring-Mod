import cv2
import numpy as np

def resize_image_with_padding(input_path, output_path, target_size, keep_alpha=True):
    """
    使用OpenCV将图片等比例缩放并添加填充到指定分辨率
    
    Args:
        input_path (str): 输入图片路径
        output_path (str): 输出图片路径
        target_size (tuple): 目标分辨率 (width, height)
        keep_alpha (bool): 是否保留透明通道
    """
    # 读取原始图片，保留alpha通道
    flags = cv2.IMREAD_UNCHANGED
    original_image = cv2.imread(input_path, flags)
    if original_image is None:
        raise ValueError(f"无法读取图片: {input_path}")
    
    # 检查是否有alpha通道
    has_alpha = original_image.shape[2] == 4 if len(original_image.shape) > 2 else False
    
    if not has_alpha and keep_alpha:
        print(f"警告: 输入图片 {input_path} 没有透明通道")
    
    # 获取原始尺寸和目标尺寸
    original_height, original_width = original_image.shape[:2]
    target_width, target_height = target_size
    
    # 计算缩放比例
    width_ratio = target_width / original_width
    height_ratio = target_height / original_height
    
    # 选择较小的缩放比例，确保图片能完整放入目标尺寸
    scale_ratio = min(width_ratio, height_ratio)
    
    # 计算缩放后的新尺寸
    new_width = int(original_width * scale_ratio)
    new_height = int(original_height * scale_ratio)
    
    # 缩放图片
    if has_alpha and keep_alpha:
        # 如果有alpha通道，分别处理RGB和A通道
        resized_image = cv2.resize(original_image, (new_width, new_height), 
                                  interpolation=cv2.INTER_LANCZOS4)
        
        # 创建透明背景画布 (RGBA)
        canvas = np.zeros((target_height, target_width, 4), dtype=np.uint8)
        canvas[:, :, 3] = 0  # 设置alpha通道为完全透明
        
        # 计算粘贴位置（居中）
        paste_x = (target_width - new_width) // 2
        paste_y = (target_height - new_height) // 2
        
        # 将缩放后的图片粘贴到画布上
        canvas[paste_y:paste_y+new_height, paste_x:paste_x+new_width] = resized_image
        
    else:
        # 如果没有alpha通道或不需要保留，转换为RGB
        if has_alpha:
            # 分离alpha通道
            rgb_image = original_image[:, :, :3]
            alpha_channel = original_image[:, :, 3]
            
            # 创建白色背景（用于处理透明区域）
            white_bg = np.ones_like(rgb_image) * 255
            
            # 归一化alpha通道
            alpha = alpha_channel[:, :, np.newaxis] / 255.0
            
            # 合成图片（alpha混合）
            rgb_image = (rgb_image * alpha + white_bg * (1 - alpha)).astype(np.uint8)
            
            # 缩放RGB图片
            resized_image = cv2.resize(rgb_image, (new_width, new_height), 
                                      interpolation=cv2.INTER_LANCZOS4)
        else:
            # 直接缩放RGB图片
            resized_image = cv2.resize(original_image, (new_width, new_height), 
                                      interpolation=cv2.INTER_LANCZOS4)
        
        # 创建黑色背景画布 (RGB)
        canvas = np.zeros((target_height, target_width, 3), dtype=np.uint8)
        
        # 计算粘贴位置（居中）
        paste_x = (target_width - new_width) // 2
        paste_y = (target_height - new_height) // 2
        
        # 将缩放后的图片粘贴到画布上
        canvas[paste_y:paste_y+new_height, paste_x:paste_x+new_width] = resized_image
    
    # 保存图片
    if has_alpha and keep_alpha:
        # 保存为PNG格式以保留透明通道
        if not output_path.lower().endswith('.png'):
            output_path = output_path.rsplit('.', 1)[0] + '.png'
        cv2.imwrite(output_path, canvas, [cv2.IMWRITE_PNG_COMPRESSION, 9])
    else:
        cv2.imwrite(output_path, canvas)
    
    print(f"图片已保存到: {output_path}")
    print(f"原始尺寸: {original_width}x{original_height}")
    print(f"目标尺寸: {target_width}x{target_height}")
    print(f"缩放后尺寸: {new_width}x{new_height}")
    print(f"填充位置: 左右各{paste_x}像素, 上下各{paste_y}像素")
    print(f"透明通道: {'保留' if (has_alpha and keep_alpha) else '不保留'}")

# 使用示例
if __name__ == "__main__":
    # 输入参数
    input_image = r"E:\code_java\DeepSpiring\tools\clip_photo\input.png"  # 输入图片路径
    output_image = "Iteration32.png"  # 输出图片路径
    target_resolution = (32, 32)  # 目标分辨率 (宽, 高)
    
    # 调用函数
    resize_image_with_padding(input_image, output_image, target_resolution, keep_alpha=True)
    
    # 其他使用示例：
    # 1. 处理RGBA图片并保留透明通道
    # resize_image_with_padding("input.png", "output.png", (512, 512), keep_alpha=True)
    
    # 2. 处理RGBA图片但不保留透明通道（转换为RGB）
    # resize_image_with_padding("input.png", "output.jpg", (512, 512), keep_alpha=False)
    
    # 3. 处理RGB图片
    # resize_image_with_padding("input.jpg", "output.jpg", (512, 512), keep_alpha=False)