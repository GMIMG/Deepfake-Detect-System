from flask import Flask, render_template, jsonify
import tensorflow as tf
from tensorflow.keras.models import load_model
from tensorflow.keras import preprocessing, models, backend as K
import numpy as np
import cv2
from mtcnn import MTCNN
import base64

app = Flask(__name__)

model = load_model('deepfake-detection2.h5')

@app.route('/predict')
def predict():
    input_shape = (160,160,3)
    mtcnn = MTCNN()
    # 영상 불러오기
    videos_path = '/static/test1.mp4'
    vid_name = videos_path.split('/')[-1].split('.')[0]
    # 프레임으로 나눠서 저장
    cap = cv2.VideoCapture(videos_path)
    frame = 0
    detect_face_num = 0

    #heat_images = []



    def get_face_coord(img):
        # 얼굴 detect
        face = mtcnn.detect_faces(img)
        # 얼굴 없으면 다음 프레임
        if not face:
            return None
        # 얼굴 위치
        x1,y1,w,h = face[0]['box']
        x2 = min(x1+w, img.shape[1])
        y2 = min(y1+h, img.shape[0])
        x1 = max(x1, 0)
        y1 = max(y1, 0)
        return [y1,y2,x1,x2]


    def crop_img(face_coord):
        # 이미지 자르기
        y1,y2,x1,x2 = face_coord
        crop_img = img[y1:y2, x1:x2]
        crop_img = cv2.resize(crop_img, (input_shape[0], input_shape[1]))
        return crop_img


    @tf.function
    def predict_and_generate_heatmap(model, img_tensor):
        # 프레임 가져와서 히트맵 표시
        conv_layer = model.get_layer("conv_7b")
        heatmap_model = models.Model([model.inputs], [conv_layer.output, model.output])
        # # Get gradient of the winner class w.r.t. the output of the (last) conv. layer
        with tf.GradientTape() as gtape:
            conv_output, predictions = heatmap_model(img_tensor)
            loss = predictions[:, tf.math.argmax(predictions[0])]
            grads = gtape.gradient(loss, conv_output)
            pooled_grads = K.mean(grads, axis=(0, 1, 2))

        heatmap = tf.reduce_mean(tf.multiply(pooled_grads, conv_output), axis=-1)
        heatmap = tf.math.maximum(heatmap, 0)
        
        max_heat = tf.math.reduce_max(heatmap)
    #     if max_heat == 0:
    #         max_heat = 1e-10
        heatmap /= max_heat
        return heatmap, predictions[0][1]




    pred = 0
    while(cap.isOpened()):
        if frame > 10 or detect_face_num > 5:
            break

        ret, img = cap.read()
        if ret == False:
            break
        
        #img = tf.convert_to_tensor(img)
        face_coord = get_face_coord(img)
        if not face_coord:
            continue
        detect_face_num+=1

        crop_image = crop_img(face_coord)

        # 이미지 전처리 및 예측
        img_tensor = (crop_image.flatten() / 255.0).reshape(-1, input_shape[0], input_shape[1], 3)
        img_tensor = tf.convert_to_tensor(img_tensor)

        heatmap, predict = predict_and_generate_heatmap(model, img_tensor)
        heatmap = np.array(heatmap)
        heatmap2 = cv2.resize(heatmap, (img_tensor.shape[2], img_tensor.shape[1]))
        heatmap2 = np.uint8(255 * heatmap2)
        heatmap2 = cv2.applyColorMap(heatmap2, cv2.COLORMAP_JET)
        hif = .5
        superimposed_img = heatmap2 * hif + crop_image
        
        #heat_images.append(superimposed_img.tolist())
        pred += predict

        # output = f'output_{vid_name}_{detect_face_num}.jpg'
        output = f'/static/heatmap.jpg'
        cv2.imwrite(output, superimposed_img)

    # 정확도 판별(평균)
    if detect_face_num:
        acc = pred/detect_face_num
    else:
        acc = 0
    acc = acc.numpy()
    acc = float(acc)

    # print(jsonify(acc=acc))
    # 정확도, 이미지 array -> json
    return jsonify(acc=acc) # ,heat_images=heat_images


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)