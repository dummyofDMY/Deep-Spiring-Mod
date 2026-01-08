from PIL import Image
import argparse
import os

def remove_white_background(input_path, output_path=None, threshold=240):
    """
    将图片中的白色背景设为透明
    
    Args:
        input_path: 输入图片路径
        output_path: 输出图片路径（默认为输入文件名+_transparent.png）
        threshold: 白色判断阈值，RGB值都大于此值被认为是白色
    """
    # 打开图片并转换为RGBA模式
    img = Image.open(input_path).convert("RGBA")
    
    # 获取图片数据
    datas = img.getdata()
    
    # 创建新的数据列表
    new_data = []
    
    for item in datas:
        # 判断是否为白色（RGB值都大于阈值）
        if item[0] > threshold and item[1] > threshold and item[2] > threshold:
            # 白色区域：alpha通道设为0（完全透明）
            new_data.append((255, 255, 255, 0))
        else:
            # 非白色区域：保持原样
            new_data.append(item)
    
    # 更新图片数据
    img.putdata(new_data)
    
    # 设置输出路径
    if output_path is None:
        name, ext = os.path.splitext(input_path)
        output_path = f"{name}_transparent.png"
    
    # 保存为PNG格式（支持透明度）
    img.save(output_path, "PNG")
    print(f"处理完成：{output_path}")
    
    return img

def batch_process(folder_path, threshold=240):
    """
    批量处理文件夹中的所有图片
    
    Args:
        folder_path: 文件夹路径
        threshold: 白色判断阈值
    """
    # 支持的图片格式
    supported_formats = ('.png', '.jpg', '.jpeg', '.bmp', '.gif')
    
    for filename in os.listdir(folder_path):
        if filename.lower().endswith(supported_formats):
            input_path = os.path.join(folder_path, filename)
            output_path = os.path.join(folder_path, f"transparent_{filename}")
            
            try:
                remove_white_background(input_path, output_path, threshold)
                print(f"已处理: {filename}")
            except Exception as e:
                print(f"处理失败 {filename}: {e}")

if __name__ == "__main__":
    # parser = argparse.ArgumentParser(description='去除图片白色背景')
    # parser.add_argument('input', help='输入图片路径或文件夹路径')
    # parser.add_argument('-o', '--output', help='输出图片路径（单张图片处理时使用）')
    # parser.add_argument('-t', '--threshold', type=int, default=240, 
    #                    help='白色判断阈值（0-255，默认240）')
    # parser.add_argument('-b', '--batch', action='store_true',
    #                    help='批量处理文件夹中的所有图片')
    
    # args = parser.parse_args()
    
    # if args.batch:
    #     # 批量处理模式
    #     batch_process(args.input, args.threshold)
    # else:
    #     # 单张图片处理模式
    #     remove_white_background(args.input, args.output, args.threshold)
    input_path = r"E:\code_java\DeepSpiring\out.png"
    output_path = r"E:\code_java\DeepSpiring\src\main\resources\DeepSpiringModResources\img\relics\.png"
    remove_white_background(input_path, output_path, 240)