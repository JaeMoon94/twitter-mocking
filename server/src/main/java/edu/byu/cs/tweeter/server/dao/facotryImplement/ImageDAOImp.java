package edu.byu.cs.tweeter.server.dao.facotryImplement;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.util.Pair;

public class ImageDAOImp implements ImageDAO {

    @Override
    public String upload(String image, String alias) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();
        final String BUCKET_NAME = "testingbyu";

        byte[] imageArray = Base64.getDecoder().decode(image);
        InputStream inputStream = new ByteArrayInputStream(imageArray);
        ObjectMetadata data = new ObjectMetadata();
        data.addUserMetadata("Content-type", "image/jpeg");
        data.setContentLength(imageArray.length);

        try{
            PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, alias, inputStream,data);
            request.withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(request);
            return s3.getUrl(BUCKET_NAME, alias).toString();

        }catch (Exception e)
        {
            System.out.println("[internal Error] Cannot upload image");
            System.out.println(e.getMessage());
            return "NOT available to upload image";
        }
    }
}
