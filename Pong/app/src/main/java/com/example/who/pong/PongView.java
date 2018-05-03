package com.example.who.pong;

class PongView extends SurfaceView implements Runnable {

    Thread mGameThread;
    SurfaceHolder mSurfaceHolder;
    volatile boolean mPlaying;
    boolean mPaused;
    Paint mPaint;
    Canvas mCanvas;
    long mFPS;
    int mScreenX;
    int mScreenY;
    Ball mBall;
    Bat mBat;
    SoundPool mSoundPool;
    int beep1;
    int beep2;
    int beep3;
    int loseLife;
    int mScore;
    int mLives;

    public PongView(Context context, int x, int y) {
        super(context);
        mScreenX = x;
        mScreenY = y;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mBat = new Bat(x, y);
        mBall = new Ball(x, y);
        mScore = 0;
        mLives = 3;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("beep1.ogg");
            beep1ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3ID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            explodeID = sp.load(descriptor, 0);

        } catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        setupAndRestart();
    }

    @Override
    public void run() {
        while (mPlaying) {
            long startFrameTime = System.currentTimeMillis();

            if(!mPaused){
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                mFPS = 1000 / timeThisFrame;
            }
        }
    }

    public void resume() {
    }

    public void pause() {
    }

    public void setupAndRestart(){
        mBall.reset(mScreenX, mScreenY);
        if(mLives == 0) {
            mScore = 0;
            mLives = 3;
        }
    }
}
