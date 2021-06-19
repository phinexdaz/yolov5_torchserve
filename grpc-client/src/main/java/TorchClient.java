import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.pytorch.serve.grpc.inference.InferenceAPIsServiceGrpc;
import org.pytorch.serve.grpc.inference.PredictionResponse;
import org.pytorch.serve.grpc.inference.PredictionsRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TorchClient {
    public static void main(String[] args) {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:3000")
                .usePlaintext()
                .build();
        InferenceAPIsServiceGrpc.InferenceAPIsServiceBlockingStub stub = InferenceAPIsServiceGrpc.newBlockingStub(channel);
        PredictionsRequest request = PredictionsRequest.newBuilder()
                .setModelName("coco128")
                .putInput("data", ByteString.copyFrom(getData())).build();

        PredictionResponse response = stub.predictions(request);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Prediction[] predictions = objectMapper.readValue(response.getPrediction().toStringUtf8(), Prediction[].class);
            for (Prediction prediction : predictions) {
                System.out.println(prediction.getLabel());
                System.out.println(prediction.getScore());
                System.out.println(Arrays.toString(prediction.getBoxes()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getData() {
        try {
            BufferedImage image = ImageIO.read(new File("E:\\coco128\\images\\train2017\\000000000025.jpg"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class Prediction {
        @JsonProperty("boxes")
        private double[] boxes;

        @JsonProperty("score")
        private double score;

        @JsonProperty("class")
        private String label;

        public double[] getBoxes() {
            return boxes;
        }

        public double getScore() {
            return score;
        }

        public String getLabel() {
            return label;
        }
    }
}
