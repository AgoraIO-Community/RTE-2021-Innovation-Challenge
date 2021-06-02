import argparse
import cv2
import numpy as np
import torch

from .models.with_mobilenet import PoseEstimationWithMobileNet
from .modules.keypoints import extract_keypoints, group_keypoints
from .modules.load_state import load_state
from .modules.pose import Pose, track_poses
from .val import normalize, pad_width
from time import perf_counter


t1=0
t2=0
flag3=0
class ImageReader(object):
    def __init__(self, file_names):
        self.file_names = file_names
        self.max_idx = len(file_names)

    def __iter__(self):
        self.idx = 0
        return self

    def __next__(self):
        if self.idx == self.max_idx:
            raise StopIteration
        img = cv2.imread(self.file_names[self.idx], cv2.IMREAD_COLOR)
        if img.size == 0:
            raise IOError('Image {} cannot be read'.format(self.file_names[self.idx]))
        self.idx = self.idx + 1
        return img


class VideoReader(object):
    def __init__(self, file_name):
        self.file_name = file_name
        try:  # OpenCV needs int to read from webcam
            self.file_name = int(file_name)
            self.cap = cv2.VideoCapture(self.file_name)
            if not self.cap.isOpened():
                raise IOError('Video {} cannot be opened'.format(self.file_name))
        except ValueError:
            pass

    def __iter__(self):
        self.cap = cv2.VideoCapture(self.file_name)
        if not self.cap.isOpened():
            raise IOError('Video {} cannot be opened'.format(self.file_name))
        return self

    def __next__(self):
        was_read, img = self.cap.read()
        if not was_read:
            raise StopIteration
        return img

    def read(self):
        was_read, img = self.cap.read()
        if not was_read:
            raise StopIteration
        return img


def infer_fast(net, img, net_input_height_size, stride, upsample_ratio, cpu,
               pad_value=(0, 0, 0), img_mean=np.array([128, 128, 128], np.float32), img_scale=np.float32(1/256)):
    height, width, _ = img.shape
    scale = net_input_height_size / height

    scaled_img = cv2.resize(img, (0, 0), fx=scale, fy=scale, interpolation=cv2.INTER_LINEAR)
    scaled_img = normalize(scaled_img, img_mean, img_scale)
    min_dims = [net_input_height_size, max(scaled_img.shape[1], net_input_height_size)]
    padded_img, pad = pad_width(scaled_img, stride, pad_value, min_dims)

    tensor_img = torch.from_numpy(padded_img).permute(2, 0, 1).unsqueeze(0).float()
    if not cpu:
        tensor_img = tensor_img.cuda()

    stages_output = net(tensor_img)

    stage2_heatmaps = stages_output[-2]
    heatmaps = np.transpose(stage2_heatmaps.squeeze().cpu().data.numpy(), (1, 2, 0))
    heatmaps = cv2.resize(heatmaps, (0, 0), fx=upsample_ratio, fy=upsample_ratio, interpolation=cv2.INTER_CUBIC)

    stage2_pafs = stages_output[-1]
    pafs = np.transpose(stage2_pafs.squeeze().cpu().data.numpy(), (1, 2, 0))
    pafs = cv2.resize(pafs, (0, 0), fx=upsample_ratio, fy=upsample_ratio, interpolation=cv2.INTER_CUBIC)

    return heatmaps, pafs, scale, pad

num_keypoints = Pose.num_kpts
previous_poses = []
def run_demo(net, img, args):
    height_size, cpu, track, smooth = args.height_size, args.cpu, args.track, args.smooth
    global t1, t2, flag3,num_keypoints,previous_poses
    stride = 8
    upsample_ratio = 4
    delay = 1
    if delay == 1:
        orig_img = img.copy()
        heatmaps, pafs, scale, pad = infer_fast(net, img, height_size, stride, upsample_ratio, cpu)

        total_keypoints_num = 0
        all_keypoints_by_type = []
        for kpt_idx in range(num_keypoints):  # 19th for bg
            total_keypoints_num += extract_keypoints(heatmaps[:, :, kpt_idx], all_keypoints_by_type, total_keypoints_num)

        pose_entries, all_keypoints = group_keypoints(all_keypoints_by_type, pafs)
        for kpt_id in range(all_keypoints.shape[0]):
            all_keypoints[kpt_id, 0] = (all_keypoints[kpt_id, 0] * stride / upsample_ratio - pad[1]) / scale
            all_keypoints[kpt_id, 1] = (all_keypoints[kpt_id, 1] * stride / upsample_ratio - pad[0]) / scale
        current_poses = []
        for n in range(len(pose_entries)):
            if len(pose_entries[n]) == 0:
                continue
            pose_keypoints = np.ones((num_keypoints, 2), dtype=np.int32) * -1
            for kpt_id in range(num_keypoints):
                if pose_entries[n][kpt_id] != -1.0:  # keypoint was found
                    pose_keypoints[kpt_id, 0] = int(all_keypoints[int(pose_entries[n][kpt_id]), 0])
                    pose_keypoints[kpt_id, 1] = int(all_keypoints[int(pose_entries[n][kpt_id]), 1])
            pose = Pose(pose_keypoints, pose_entries[n][18])
            current_poses.append(pose)

        if track:
            track_poses(previous_poses, current_poses, smooth=smooth)
            previous_poses = current_poses
        for pose in current_poses:
            pose.draw(img)

#################
        message = ''
        ###
        len1 = abs(pose.keypoints[2][0] - pose.keypoints[5][0])
        len2 = abs(pose.keypoints[2][1] -pose.keypoints[5][1])
        Len = (len1 * len1 + len2 * len2) ** 0.5
        # 站
        judge1 = 0
        judge2 = 0
        flag1 = 0
        flag2 = 0
        if abs(pose.keypoints[8][0] - pose.keypoints[9][0]) < Len / 2 and abs(pose.keypoints[9][0] - pose.keypoints[10][0]) < Len / 2:
            if abs(pose.keypoints[2][0] - pose.keypoints[8][0]) < Len:
                judge1 = 1
        if abs(pose.keypoints[11][0] - pose.keypoints[12][0]) < Len / 2 and abs(pose.keypoints[12][0] - pose.keypoints[13][0]) < Len / 2:
            if abs(pose.keypoints[5][0] - pose.keypoints[11][0]) < Len:
                judge2 = 1
        if judge1 and judge2:
            message = 'stand'
            #cv2.putText(img, message, (200, 200), cv2.FONT_HERSHEY_COMPLEX, 3.5, (255, 0, 0), 2)
            flag1 = 1
        max_h = 0
        min_h = 10000
        for jj in range(16):
            if pose.keypoints[jj][1]>0:
                if pose.keypoints[jj][1] > max_h :
                    max_h = pose.keypoints[jj][1]
                if pose.keypoints[jj][1] < min_h:
                    min_h = pose.keypoints[jj][1]
        # 躺
        high = max_h - min_h
        print(high)
        print(pose.keypoints[0],pose.keypoints[13])

        if high < int(img.shape[0] / 3):
            message = 'lie'
            #cv2.putText(img, message, (200, 200), cv2.FONT_HERSHEY_COMPLEX, 3.5, (0, 0, 255), 2)
            flag2 = 1

        if flag2==1 and flag3==0:
            t1=cv2.getTickCount()
            flag3=1
        if flag2==1 and flag3==1:
            t2=cv2.getTickCount()
            t = (t2 - t1) / cv2.getTickFrequency()
        if message != "stand":
            if t>10:
                message = 'danger'
                cv2.putText(img, message, (150,150), cv2.FONT_HERSHEY_COMPLEX, 4.5, (0, 0, 255), 2)


################
        img = cv2.addWeighted(orig_img, 0.6, img, 0.4, 0)
        for pose in current_poses:
            cv2.rectangle(img, (pose.bbox[0], pose.bbox[1]),
                          (pose.bbox[0] + pose.bbox[2], pose.bbox[1] + pose.bbox[3]), (0, 255, 0))
            if track:
                cv2.putText(img, 'id: {}'.format(pose.id), (pose.bbox[0], pose.bbox[1] - 16),
                            cv2.FONT_HERSHEY_COMPLEX, 0.5, (0, 0, 255))
        #cv2.imshow('Lightweight Human Pose Estimation Python Demo', img)
        # key = cv2.waitKey(delay)
        # if key == 27:  # esc
        #     return
        # elif key == 112:  # 'p'
        #     if delay == 1:
        #         delay = 0
        #     else:
        #         delay = 1
    return message

def load_Info():
    parser = argparse.ArgumentParser(
        description='''Lightweight human pose estimation python demo.
                           This is just for quick results preview.
                           Please, consider c++ demo for the best performance.''')
    parser.add_argument('--checkpoint-path', type=str, default='E:\work\AgoraLive\mypose\checkpoint_iter_370000.pth',
                        help='path to the checkpoint')  # 到检查点的路
    parser.add_argument('--height-size', type=int, default=256, help='network input layer height size')  # 网络输入层高度大小
    parser.add_argument('--video', type=str, default='E:\work\AgoraLive\mypose\s2.mp4',
                        help='path to video file or camera id')  # 视频文件或摄像机id的路径
    parser.add_argument('--images', nargs='+', default='', help='path to input image(s)')
    parser.add_argument('--cpu', action='store_true', help='run network inference on cpu')
    parser.add_argument('--track', type=int, default=1, help='track pose id in video')  # 在视频中跟踪姿势id
    parser.add_argument('--smooth', type=int, default=1, help='smooth pose keypoints')  # 平滑姿势关键点
    args = parser.parse_args()
    if args.video == '' and args.images == '':
        raise ValueError('Either --video or --image has to be provided')

    net = PoseEstimationWithMobileNet()
    checkpoint = torch.load(args.checkpoint_path, map_location='cpu')
    load_state(net, checkpoint)

    #frame_provider = ImageReader(args.images)
    if args.video != '':
        cap = cv2.VideoCapture(args.video)
        if not cap.isOpened():
            raise IOError('Video {} cannot be opened'.format(args.video))
    else:
        args.track = 0
    net = net.eval()
    if not args.cpu:
        net = net.cuda()
    return net, cap, args
