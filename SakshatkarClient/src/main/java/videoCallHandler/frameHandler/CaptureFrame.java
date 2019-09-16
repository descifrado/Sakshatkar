package videoCallHandler.frameHandler;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CaptureFrame {
    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture(0);
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;
    private DatagramSocket socket;

    public CaptureFrame()
    {
        boolean wset = capture.set(HighGui.WINDOW_NORMAL,180);
        boolean hset = capture.set(HighGui.WINDOW_NORMAL,180);
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private Mat grabFrame()
    {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, process it
                if (!frame.empty())
                {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }

            }
            catch (Exception e)
            {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }
        return frame;
    }

    private void stopAcquisition()
    {
        if (this.timer!=null && !this.timer.isShutdown())
        {
            try
            {
                    // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                    // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened())
        {
                // release the camera
            this.capture.release();
        }
    }
    public void setClosed()
    {
        this.stopAcquisition();
    }
    public void startCam(InetAddress address){
        if (!this.cameraActive)
        {
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened())
            {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run()
                    {
                        // effectively grab and process a single frame
                        Mat frameMat = grabFrame();
                        Size size=new Size(180,180);
                        Mat finalFrame=new Mat();
                        Imgproc.resize(frameMat,finalFrame,size);
                        frameMat=finalFrame;
                        MatWrapper frame=new MatWrapper(frameMat);
                        // convert and show the frame
                        byte[] framePacket;
                        try {
                            ByteArrayOutputStream bos=new ByteArrayOutputStream();
                            ObjectOutput output=null;
                            try {
                                output=new ObjectOutputStream(bos);
                                output.writeObject(frame);
                                output.flush();
                                framePacket=bos.toByteArray();
                            }finally {
                                try {
                                    bos.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            System.out.println(framePacket.length);
                            DatagramPacket packet=new DatagramPacket(framePacket,framePacket.length,address,7000);
                            socket.send(packet);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

                // update the button content
            }
            else
            {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
        }
        else
        {
            // the camera is not active at this point
            this.cameraActive = false;
            // update again the button content

            // stop the timer
            this.stopAcquisition();
        }
    }

}

