# yolov5_torchserve

## archive model
```
cd yolov5_mar 
zip -r ../my_model.mar .
cd ..
mkdir model-store
mv my_model.mar model-store/
```

## start torchserve
```
docker run --rm -it -p 3000:7070 -v $(pwd)/model-store:/home/model-server/model-store yolov5/torchserve:latest torchserve --start --model-store model-store --models my_model=my_model.mar
```
