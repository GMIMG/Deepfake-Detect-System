from flask import Flask, render_template, jsonify
import os
import time
import numpy as np
import pandas as pd
import tensorflow as tf
import cv2 as cv2
import cv2 as cv
# from keras.preprocessing.image import ImageDataGenerator, img_to_array
from tensorflow.keras.models import Sequential, load_model
from facenet_pytorch import MTCNN
from sklearn.metrics import log_loss
from os.path import join
# from tensorflow.keras.optimizers import Adam

app = Flask(__name__)

# model = load_model('deepfake-detection2.h5')
model = load_model("Xcept-55-0.0002.h5")
# model.compile(loss='binary_crossentropy', optimizer="adam", metrics=['acc'])

@app.route('/predict')
def predict():

    input_shape = (160,160,3)
    # Text variables
    font_face = cv2.FONT_HERSHEY_SIMPLEX
    thickness = 2
    font_scale = 1


    # 얼굴 좌표찾기
    def get_face_coord(img, detector):
        # 얼굴 detect
        face = detector.detect_faces(img)
        # 얼굴 없으면 다음 프레임
        if not face:
            return None
        # 얼굴 위치
        x1,y1,w,h = face[0]['box']
        x2 = min(x1+w, img.shape[1])
        y2 = min(y1+h, img.shape[0])
        x1 = max(x1, 0)
        y1 = max(y1, 0)
        return (y1,y2,x1,x2)

    # 이미지 자르기
    def crop_img(img, face_coord):
        y1,y2,x1,x2 = face_coord
        crop_img = img[y1:y2, x1:x2]
        crop_img = cv2.resize(crop_img, (input_shape[0], input_shape[1]))
        return crop_img

    def draw_face_box(image, face_coord, prediction, writer):
        y1,y2,x1,x2 = face_coord
        color = (0, 255, 0) if prediction == 0 else (0, 0, 255)
        label = 'fake' if prediction == 1 else 'real'
        # output_list = ['{0:.2f}'.format(float(x)) for x in output.detach().cpu().numpy()[0]]
        cv2.putText(image, str(prediction)+'=>'+label, (x1, y2+30),
                    font_face, font_scale,
                    color, thickness, 2)
        # draw box over face
        cv2.rectangle(image, (x1, y1), (x2, y2), color, 2)
        writer.write(image)

        if writer is not None:
            writer.release()







    def predict_model(video_path, model, start_frame=0, end_frame=None, n_frames=5): #, frame_step=1,
        
        # assert start_frame < num_frames - 1

        # 비디오 불러오기
        reader = cv2.VideoCapture(video_path) # '/static/test1.mp4'

        # 영상의 프레임수 구하기
        num_frames = int(reader.get(cv2.CAP_PROP_FRAME_COUNT))

        # 가져올 프레임 결정
        end_frame = end_frame if end_frame else num_frames
        pred_frames = [int(round(x)) for x in np.linspace(start_frame, end_frame, n_frames)]

        # 영상 이름
        video_fn = video_path.split('/')[-1].split('.')[0] + '.avi'

        count = 0
        predictions = []
        outputs = []
        writer = None
        fourcc = cv2.VideoWriter_fourcc(*'MJPG')
        fps = reader.get(cv2.CAP_PROP_FPS)
        output_path = '/static/'
        # output_path = './'

        detector = MTCNN()
        # detector = MTCNN(margin=50, keep_all=False, post_process=False,thresholds=[.9,.9,.9]) # , device='cuda:0'


        frame_num = 0
        while reader.isOpened():
            success = reader.grab()

            # 가져올 프레임 안에 있으면
            if frame_num in pred_frames:
                _, frame = reader.retrieve()

                height, width = frame.shape[:2]
                if writer is None:
                    writer = cv2.VideoWriter(join(output_path, video_fn), fourcc, fps, (width, height))

                face_coord = get_face_coord(frame, detector)
                if not face_coord:
                    continue
                count += 1
                print(count)

                croped_img = crop_img(frame, face_coord)

                # frame = cv.cvtColor(frame, cv.COLOR_BGR2RGB)
                # frame = np.transpose(frame, (1, 2, 0))
                croped_img = np.array(cv.resize(np.array(croped_img),(160 ,160)))
                croped_img = (croped_img.flatten() / 255.0).reshape(-1, 160, 160, 3)

                predict = model.predict(croped_img)[0][0]
                print('predict',predict)
                prediction = 1 if predict > 0.5 else 0
                print('prediction',prediction)
                predictions.append(predict)

                draw_face_box(frame, face_coord, prediction)
            frame_num += 1
        return predictions, count



    path = '/predict/'
    # path = './'

    predictions, count = predict_model( path + 'test.mp4', model, start_frame=0, end_frame=None, n_frames=5)
    if (count>11):
        predictions.sort()
        final_pred = sum(predictions[5:-5])/(count-10)
        if final_pred==1: final_pred=0.99
        elif final_pred==0: final_pred=0.01
    else:
        final_pred=0.5
    # release()

    # acc = (int(final_pred*10000))/100
    acc = final_pred
    return jsonify(acc=acc)











if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)