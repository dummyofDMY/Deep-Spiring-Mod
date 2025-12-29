import cv2
import numpy as np

def resize_image_with_crop(input_path, output_path, target_size, keep_alpha=True, crop_position='center'):
    """
    使用OpenCV将图片等比例缩放并通过裁剪到指定分辨率
    
    Args:
        input_path (str): 输入图片路径
        output_path (str): 输出图片路径
        target_size (tuple): 目标分辨率 (width, height)
        keep_alpha (bool): 是否保留透明通道
        crop_position (str): 裁剪位置，可选 'center'(居中), 'top'(顶部), 'bottom'(底部), 'left'(左边), 'right'(右边)
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
    
    # 选择较大的缩放比例，确保图片能覆盖目标尺寸
    scale_ratio = max(width_ratio, height_ratio)
    
    # 计算缩放后的新尺寸
    new_width = int(original_width * scale_ratio)
    new_height = int(original_height * scale_ratio)
    
    # 缩放图片
    if has_alpha and keep_alpha:
        # 如果有alpha通道，分别处理RGB和A通道
        scaled_image = cv2.resize(original_image, (new_width, new_height), 
                                 interpolation=cv2.INTER_LANCZOS4)
        
        # 计算裁剪位置
        crop_x, crop_y = calculate_crop_position(new_width, new_height, 
                                                target_width, target_height, 
                                                crop_position)
        
        # 裁剪图片
        cropped_image = scaled_image[crop_y:crop_y+target_height, 
                                    crop_x:crop_x+target_width]
        
    else:
        # 如果没有alpha通道或不需要保留
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
            scaled_image = cv2.resize(rgb_image, (new_width, new_height), 
                                     interpolation=cv2.INTER_LANCZOS4)
        else:
            # 直接缩放RGB图片
            scaled_image = cv2.resize(original_image, (new_width, new_height), 
                                     interpolation=cv2.INTER_LANCZOS4)
        
        # 计算裁剪位置
        crop_x, crop_y = calculate_crop_position(new_width, new_height, 
                                                target_width, target_height, 
                                                crop_position)
        
        # 裁剪图片
        cropped_image = scaled_image[crop_y:crop_y+target_height, 
                                    crop_x:crop_x+target_width]
    
    # 保存图片
    if has_alpha and keep_alpha:
        # 保存为PNG格式以保留透明通道
        if not output_path.lower().endswith('.png'):
            output_path = output_path.rsplit('.', 1)[0] + '.png'
        cv2.imwrite(output_path, cropped_image, [cv2.IMWRITE_PNG_COMPRESSION, 9])
    else:
        cv2.imwrite(output_path, cropped_image)
    
    print(f"图片已保存到: {output_path}")
    print(f"原始尺寸: {original_width}x{original_height}")
    print(f"目标尺寸: {target_width}x{target_height}")
    print(f"缩放后尺寸: {new_width}x{new_height}")
    print(f"裁剪位置: 从({crop_x}, {crop_y})开始")
    print(f"裁剪大小: {target_width}x{target_height}")
    print(f"透明通道: {'保留' if (has_alpha and keep_alpha) else '不保留'}")

def calculate_crop_position(src_width, src_height, target_width, target_height, position='center'):
    """
    计算裁剪起始位置
    
    Args:
        src_width, src_height: 源图片宽高
        target_width, target_height: 目标图片宽高
        position: 裁剪位置
    """
    # 计算可裁剪的范围
    crop_width = src_width - target_width
    crop_height = src_height - target_height
    
    # 根据指定的位置计算裁剪起始点
    if crop_width <= 0 and crop_height <= 0:
        # 如果缩放后图片比目标小，则居中放置
        crop_x = max(0, (target_width - src_width) // 2)
        crop_y = max(0, (target_height - src_height) // 2)
    else:
        if position == 'center':
            crop_x = max(0, crop_width // 2)
            crop_y = max(0, crop_height // 2)
        elif position == 'top':
            crop_x = max(0, crop_width // 2)
            crop_y = 0
        elif position == 'bottom':
            crop_x = max(0, crop_width // 2)
            crop_y = max(0, crop_height)
        elif position == 'left':
            crop_x = 0
            crop_y = max(0, crop_height // 2)
        elif position == 'right':
            crop_x = max(0, crop_width)
            crop_y = max(0, crop_height // 2)
        elif position == 'top-left':
            crop_x = 0
            crop_y = 0
        elif position == 'top-right':
            crop_x = max(0, crop_width)
            crop_y = 0
        elif position == 'bottom-left':
            crop_x = 0
            crop_y = max(0, crop_height)
        elif position == 'bottom-right':
            crop_x = max(0, crop_width)
            crop_y = max(0, crop_height)
        else:
            crop_x = max(0, crop_width // 2)
            crop_y = max(0, crop_height // 2)
    
    return crop_x, crop_y

# 使用示例
if __name__ == "__main__":
    # 输入参数
    input_image = r"E:\code_java\DeepSpiring\tools\clip_photo\input.png"  # 输入图片路径
    output_image = "out.png"  # 输出图片路径
    target_resolution = (250, 190)  # 目标分辨率 (宽, 高)
    target_resolution = (500, 380)
    
    # 调用函数，可选的裁剪位置：
    # 'center'(默认), 'top', 'bottom', 'left', 'right'
    # 'top-left', 'top-right', 'bottom-left', 'bottom-right'
    resize_image_with_crop(input_image, output_image, target_resolution, 
                          keep_alpha=True, crop_position='center')
    
    # 其他使用示例：
    # 1. 从顶部裁剪
    # resize_image_with_crop(input_image, "output_top.png", target_resolution, 
    #                       keep_alpha=True, crop_position='bottom')
    
    # 2. 从左上角裁剪
    # resize_image_with_crop("input.png", "output_topleft.png", (512, 512), 
    #                       keep_alpha=True, crop_position='top-left')
    
    # 3. 不保留透明通道
    # resize_image_with_crop("input.png", "output.jpg", (512, 512), 
    #                       keep_alpha=False, crop_position='center')