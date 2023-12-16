import cv2
import matplotlib.pyplot as plt
import tensorflow
import numpy as np


def find_frames_timestamps(video_path, video_segmentation=2):
    video = cv2.VideoCapture(video_path)
    if not video.isOpened():
        print("The file can not be open!")
        return
    face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

    frames = []
    timestamps = []
    segment_size = 0
    while True:
        ret, frame = video.read()
        if not ret:
            video.release()
            break

        if segment_size == video_segmentation:

            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
            faces = face_cascade.detectMultiScale(frame, scaleFactor=1.2, minNeighbors=5)
            if len(faces) >= 1:
                x = faces[0][0]
                y = faces[0][1]
                w = faces[0][2]
                h = faces[0][3]
                face = frame[y:y + h, x:x + w].astype('float32') / 255.0
                face = np.array(face)
                face_resized = cv2.resize(face, (48, 48))
                face_resized = np.expand_dims(face_resized, axis=-1)
                face_resized = np.repeat(face_resized, 3, axis=-1)
                frames.append(face_resized)
                timestamp = video.get(cv2.CAP_PROP_POS_MSEC)

                minutes = int(timestamp / (1000 * 60))
                seconds = int((timestamp / 1000) % 60)
                milliseconds = int(timestamp % 1000)

                timestamps.append((minutes, seconds, milliseconds))
                segment_size = 0
        else:
            segment_size = segment_size + 1

    return frames, timestamps


def predict_for_frames(frames):
    model = tensorflow.keras.models.load_model('face_model/model.h5')
    predictions = []
    for frame in frames:
        frame = frame.reshape(1, *frame.shape)
        prediction = model.predict(frame)
        predictions.append(prediction)
    return predictions


def plotGraphic(predictions, timestamps, output_path):
    emotions = {0: 'angry', 1: 'disgust', 2: 'fear', 3: 'happy', 4: 'neutral', 5: 'sad', 6: 'surprise'}
    markers = {0: '*', 1: 'o', 2: 's', 3: '^', 4: 'D', 5: 'p', 6: 'h'}

    plt.figure(figsize=(15, 5))

    timestamps_sec = [t[0] * 60 + t[1] + t[2] / 1000 for t in timestamps]

    for emotion_id in emotions.keys():
        plt.plot(timestamps_sec, [pred[0][emotion_id] for pred in predictions], linestyle=':', linewidth=0.5,
                 label=emotions[emotion_id], marker=markers[emotion_id], markersize=4)

    plt.xlabel('Time(seconds)')
    plt.ylabel('Confidence')
    plt.title('The evolution of emotions in time')
    plt.legend()
    plt.savefig(output_path, bbox_inches='tight')
    plt.show()


def predict_for_video(video_path, output_path):
    frames, timestamps = find_frames_timestamps(video_path)
    predictions = predict_for_frames(frames)
    plotGraphic(predictions, timestamps, output_path)

if  __name__ == "__main__":
    video_path = 'data/disgust1.mp4'
    output_path = 'output.png'
    predict_for_video(video_path, output_path)