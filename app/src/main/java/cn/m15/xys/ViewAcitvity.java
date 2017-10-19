package cn.m15.xys;

import java.io.InputStream;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
public class ViewAcitvity extends Activity{

    AnimView mAnimView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	// ȫ����ʾ����
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
	// ��ȡ��Ļ���
	Display display = getWindowManager().getDefaultDisplay();
	
	// ��ʾ�Զ������ϷView
	mAnimView = new AnimView(this,display.getWidth(), display.getHeight());
	setContentView(mAnimView);
    }

    public class AnimView extends View implements Runnable{
	/**�����ƶ�����**/
        public final static int ANIM_DOWN = 0;
        /**�����ƶ�����**/
        public final static int ANIM_LEFT = 1;
        /**�����ƶ�����**/
        public final static int ANIM_RIGHT = 2;
        /**�����ƶ�����**/
        public final static int ANIM_UP = 3;
        /**������������**/
        public final static int ANIM_COUNT = 4;
	
        Animation mHeroAnim [] = new Animation[ANIM_COUNT];
        
        Paint mPaint = null;
	
	/**�����������**/
	private boolean mAllkeyDown = false;
	/**������**/
	private boolean mIskeyDown = false;
	/**������**/
	private boolean mIskeyLeft = false;
	/**������**/
	private boolean mIskeyRight = false;
	/**������**/
	private boolean mIskeyUp = false;
	
	//��ǰ���ƶ���״̬ID
	int mAnimationState = 0;
	
        //tile��Ŀ��
	public final static int TILE_WIDTH = 32;
	public final static int TILE_HEIGHT = 32;
	
        //tile��Ŀ�ߵ�����
	public final static int TILE_WIDTH_COUNT = 10;
	public final static int TILE_HEIGHT_COUNT = 15;
	
	//����Ԫ��Ϊ0��ʲô������
	public final static int TILE_NULL = 0;
	//��һ����ϷView��ͼ����
	public int [][]mMapView = {
		{ 1, 1, 1, 1, 137, 137, 137, 1, 1, 1 },
		{ 1, 1, 1, 1, 137, 137, 137, 1, 1, 1 },
		{ 1, 1, 1, 1, 137, 137, 137, 1, 1, 1 },
		{ 137, 137, 137, 137, 137, 137, 137, 137, 137, 137 },
		{ 137, 137, 137, 137, 137, 137, 137, 137, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 137, 137 },
		{ 137, 137, 137, 137, 137, 137, 137, 137, 137, 137 },
		{ 137, 137, 137, 137, 137, 137, 137, 137, 137, 137 },
		{ 1, 1, 1, 1, 1, 137, 137, 137, 1, 1 },
		{ 1, 1, 1, 1, 1, 137, 137, 137, 1, 1 }
		};

	//�ڶ�����Ϸʵ��actor����
	public int [][]mMapAcotor  = {
		{ 102, 103, 103, 104, 0, 0, 0, 165, 166, 167 },
		{ 110, 111, 111, 112, 0, 0, 0, 173, 174, 175 },
		{ 126, 127, 127, 128, 0, 0, 0, 181, 182, 183 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 41, 42, 43, 44, 0, 0, 0, 0, 0, 0 },
		{ 49, 50, 51, 52, 0, 0, 0, 0, 0, 0 },
		{ 57, 58, 59, 60, 229, 230, 231, 232, 0, 0 },
		{ 65, 66, 67, 68, 237, 238, 239, 240, 0, 0 },
		{ 0, 0, 0, 0, 245, 246, 247, 248, 0, 0 },
		{ 0, 0, 0, 0, 0, 254, 255, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 102, 103, 103, 103, 104, 0, 0, 0, 143, 144 },
		{ 110, 111, 111, 111, 112, 0, 0, 0, 143, 144 }
		};
	
	//��������Ϸ��ײ��������� 
	public int [][]mCollision  = {
		{ -1, -1, -1, -1, 0, 0, 0, -1, -1, -1 },
		{ -1, -1, -1, -1, 0, 0, 0, -1, -1, -1 },
		{ -1, -1, -1, -1, 0, 0, 0, -1, -1, -1 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0 },
		{ -1, -1, -1, -1, 0, 0, 0, 0, 0, 0 },
		{ -1, -1, -1, -1, -1, -1, -1, -1, 0, 0 },
		{ -1, -1, -1, -1, -1, -1, -1, -1, 0, 0 },
		{ 0, 0, 0, 0, -1, -1, -1, -1, 0, 0 },
		{ 0, 0, 0, 0, 0, -1, -1, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
		{ -1, -1, -1, -1, -1, 0, 0, 0, -1, -1 },
		{ -1, -1, -1, -1, -1, 0, 0, 0, -1, -1 }
		};
	
	//��Ϸ��ͼ��Դ
	Bitmap mBitmap = null;
	
	//��Դ�ļ�
	Resources mResources = null;
	
	//��������tile�������
	int mWidthTileCount = 0;
	int mHeightTileCount = 0;

	//��������tile�������
	int mBitMapWidth = 0;
	int mBitMapHeight = 0;
	
	//Ӣ���ڵ�ͼ�е�������Ӣ�۽ŵ�����Ϊԭ��
	int mHeroPosX = 0;
	int mHeroPosY= 0;
	
	//����Ӣ�۷�����ײ��ǰ�������
	int mBackHeroPosX = 0;
	int mBackHeroPosY= 0;
	
	//Ӣ���ڵ�ͼ�л�������
	int mHeroImageX = 0;
	int mHeroImageY= 0;
	
	//Ӣ���ڵ�ͼ��λ�����е�����
	int mHeroIndexX = 0;
	int mHeroIndexY= 0;
	
	//��Ļ��߲ųߴ�
	int mScreenWidth = 0;
	int mScreenHeight = 0;
	
	/**����ͼƬ��Դ��ʵ��Ӣ�۽ŵװ������ƫ��**/
	public final static int OFF_HERO_X = 16;
	public final static int OFF_HERO_Y = 35;
	
	/**�������߲���**/
	public final static int HERO_STEP = 8;
	
	
	/**��ʵ��㷢����ײ**/
	private boolean isAcotrCollision = false;
	/**��߽�㷢����ײ**/
	private boolean isBorderCollision = false;
	/**�����﷢����ײ**/
	private boolean isPersonCollision = false;
	
	
	/**˫����Bitmap**/
	private Bitmap mBufferBitmap = null;
	/**˫���廭��**/
	private Canvas mCanvas = null;
	/**��Ϸ���߳�**/
	private Thread mThread = null;
	/**�߳�ѭ����־**/
	private boolean mIsRunning = false;
	/**
	 * ���췽��
	 * 
	 * @param context
	 */
	public AnimView(Context context,int screenWidth, int screenHeight) {
	    super(context);
	    mPaint = new Paint();
	    mScreenWidth = screenWidth;
	    mScreenHeight = screenHeight;
	    /*˫����**/
	    mBufferBitmap = Bitmap.createBitmap(mScreenWidth,mScreenHeight,Config.ARGB_8888);
	    mCanvas = new Canvas();
	    /**bitmap������˫����Ļ�����**/
	    mCanvas.setBitmap(mBufferBitmap);
	    initAnimation(context);
	    initMap(context);
	    initHero();
	    /**������Ϸ���߳�**/
	    mIsRunning = true;
	    mThread = new Thread(this);
	    mThread.start();
	   
	}

	private void initHero() {
	    mHeroImageX = 100;
	    mHeroImageY = 100;
	    /**����ͼƬ��ʾ���������Ӣ�۽ŵ׵����� **/
	    /**X��+ͼƬ��ȵ�һ�� Y���ͼƬ�ĸ߶� **/
	    mHeroPosX = mHeroImageX + OFF_HERO_X; 
	    mHeroPosY = mHeroImageY + OFF_HERO_Y;
	    mHeroIndexX = mHeroPosX / TILE_WIDTH;
	    mHeroIndexY = mHeroPosY / TILE_HEIGHT;
	}

	private void initMap(Context context) {
	    mBitmap = ReadBitMap(context, R.drawable.map);
	    mBitMapWidth = mBitmap.getWidth();
	    mBitMapHeight = mBitmap.getHeight();
	    mWidthTileCount = mBitMapWidth / TILE_WIDTH;
	    mHeightTileCount = mBitMapHeight / TILE_HEIGHT;
	}

	private void initAnimation(Context context) {
	    //���������ѭ����������֮������Ҫ�Ѷ�����ID����ȥ
	    mHeroAnim[ANIM_DOWN] = new Animation(context,new int []{R.drawable.hero_down_a,R.drawable.hero_down_b,R.drawable.hero_down_c,R.drawable.hero_down_d},true);
	    mHeroAnim[ANIM_LEFT] = new Animation(context,new int []{R.drawable.hero_left_a,R.drawable.hero_left_b,R.drawable.hero_left_c,R.drawable.hero_left_d},true);
	    mHeroAnim[ANIM_RIGHT]= new Animation(context,new int []{R.drawable.hero_right_a,R.drawable.hero_right_b,R.drawable.hero_right_c,R.drawable.hero_right_d},true);
	    mHeroAnim[ANIM_UP]   = new Animation(context,new int []{R.drawable.hero_up_a,R.drawable.hero_up_b,R.drawable.hero_up_c,R.drawable.hero_up_d},true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
	    /**�����Ȱ�������Ҫ���Ƶ���Դ���Ƶ�mBufferBitmap��**/
	    /**���Ƶ�ͼ**/
	    DrawMap(mCanvas,mPaint,mBitmap);
	    /**���ƶ���**/
	    RenderAnimation(mCanvas);
	    /**���¶���**/
	    UpdateAnimation();
	    
	    
	    if(isBorderCollision) {
		DrawCollision(mCanvas,"��߽緢����ײ");
	    }
	    
	    if(isAcotrCollision) {
		DrawCollision(mCanvas,"��ʵ��㷢����ײ");
	    }
	    if(isPersonCollision) {
		DrawCollision(mCanvas,"��NPC������ײ");
	    }
	    
	    /**���ͨ��canvasһ���Եİ�mBufferBitmap���Ƶ���Ļ��**/
	    canvas.drawBitmap(mBufferBitmap, 0,0, mPaint);
	    super.onDraw(canvas);
	}

	private void DrawCollision(Canvas canvas ,String str) {
	    drawRimString(canvas, str,  Color.WHITE,mScreenWidth >> 1, mScreenHeight >> 1);
	}
	
	private void UpdateAnimation() {
	    if (mAllkeyDown) {
		/** ���ݰ���������ʾ���� **/
		/** ����ײ������Ѱ�ҿ��Լ��Ƿ����ͼ����㷢����ײ **/
		if (mIskeyDown) {
		    mAnimationState = ANIM_DOWN;
		    mHeroPosY += HERO_STEP;
		} else if (mIskeyLeft) {
		    mAnimationState = ANIM_LEFT;
		    mHeroPosX -= HERO_STEP;
		} else if (mIskeyRight) {
		    mAnimationState = ANIM_RIGHT;
		    mHeroPosX += HERO_STEP;
		} else if (mIskeyUp) {
		    mAnimationState = ANIM_UP;
		    mHeroPosY -= HERO_STEP;
		}

		/** ��������Ƿ���� **/
		isBorderCollision = false;
		if (mHeroPosX <= 0) {
		    mHeroPosX = 0;
		    isBorderCollision =true;
		} else if (mHeroPosX >= mScreenWidth) {
		    mHeroPosX = mScreenWidth;
		    isBorderCollision =true;
		}
		if (mHeroPosY <= 0) {
		    mHeroPosY = 0;
		    isBorderCollision =true;
		} else if (mHeroPosY >= mScreenHeight) {
		    mHeroPosY = mScreenHeight;
		    isBorderCollision =true;
		}

		/** ���Ӣ���ƶ����ڵ�ͼ��λ�����е����� **/
		mHeroIndexX = mHeroPosX / TILE_WIDTH;
		mHeroIndexY = mHeroPosY / TILE_HEIGHT;

		/** Խ���� **/
		int width = mCollision[0].length - 1;
		int height = mCollision.length - 1;

		if (mHeroIndexX <= 0) {
		    mHeroIndexX = 0;
		} else if (mHeroIndexX >= width) {
		    mHeroIndexX = width;
		}
		if (mHeroIndexY <= 0) {
		    mHeroIndexY = 0;
		} else if (mHeroIndexY >= height) {
		    mHeroIndexY = height;
		}
		if (mCollision[mHeroIndexY][mHeroIndexX] == -1) {
		    mHeroPosX = mBackHeroPosX;
		    mHeroPosY = mBackHeroPosY;
		    isAcotrCollision = true;
		} else {
		    mBackHeroPosX = mHeroPosX;
		    mBackHeroPosY = mHeroPosY;
		    isAcotrCollision = false;
		}
		/** ���������Ƶ�XY���� **/
		mHeroImageX = mHeroPosX - OFF_HERO_X;
		mHeroImageY = mHeroPosY - OFF_HERO_Y;
	    }
	}
	private void RenderAnimation(Canvas canvas) {
	    if (mAllkeyDown) {
		/**�������Ƕ���**/
		mHeroAnim[mAnimationState].DrawAnimation(canvas, mPaint, mHeroImageX, mHeroImageY);
	    }else {
		/**����̧�������ֹͣ����**/
		mHeroAnim[mAnimationState].DrawFrame(canvas, mPaint, mHeroImageX, mHeroImageY, 0);
	    }
	}
	
	/**
	 * ���ð���״̬trueΪ���� falseΪ̧��
	 * @param keyCode
	 * @param state
	 */
        public void setKeyState(int keyCode, boolean state) {
            switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
        	mIskeyDown = state;
        	break;
            case KeyEvent.KEYCODE_DPAD_UP:
        	mIskeyUp = state;
        	break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
        	mIskeyLeft = state;
        	break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
        	mIskeyRight = state;
        	break;
            }
            mAllkeyDown = state;
        }
        private void DrawMap(Canvas canvas,Paint paint ,Bitmap bitmap) {
            int i,j;
            for(i = 0; i< TILE_HEIGHT_COUNT; i++) {
        	for(j = 0; j<TILE_WIDTH_COUNT;j++) {
        	    int ViewID =  mMapView[i][j];
        	    int ActorID = mMapAcotor[i][j];
        	    //���Ƶ�ͼ��һ��
        	    if(ViewID > TILE_NULL) {
        		 DrawMapTile(ViewID,canvas,paint,bitmap, j * TILE_WIDTH , i * TILE_HEIGHT);
        	    }
        	   
        	    //���Ƶ�ͼ�ڶ���
        	    if(ActorID > TILE_NULL) {
        		DrawMapTile(ActorID,canvas,paint,bitmap, j * TILE_WIDTH , i * TILE_HEIGHT);
        	    }
        	}
            }
        }
        
        
        
        /**
         * ����ID����һ��tile��
         * @param id
         * @param canvas
         * @param paint
         * @param bitmap
         */
        private void DrawMapTile(int id,Canvas canvas,Paint paint ,Bitmap bitmap,int x, int y) {
            //���������е�ID����ڵ�ͼ��Դ�е�XY ����
            //��Ϊ�༭��Ĭ��0 ���Ե�һ��tile��ID����0����1 �������� -1
            id--;
            int count = id /mWidthTileCount;
            int bitmapX = (id - (count * mWidthTileCount)) * TILE_WIDTH;
            int bitmapY = count * TILE_HEIGHT;
            DrawClipImage(canvas,paint,bitmap,x,y,bitmapX,bitmapY,TILE_WIDTH,TILE_HEIGHT);
        }
        
        /**
	 * ����ͼƬ�е�һ����ͼƬ
	 * @param canvas
	 * @param paint
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param src_x
	 * @param src_y
	 * @param src_width
	 * @param src_Height
	 */
	private void DrawClipImage(Canvas canvas,Paint paint ,Bitmap bitmap, int x, int y, int src_x, int src_y, int src_xp, int src_yp) {
	    canvas.save();
	    canvas.clipRect(x, y, x + src_xp, y + src_yp);
	    canvas.drawBitmap(bitmap, x - src_x, y - src_y,paint);
	    canvas.restore();
	}
	
        /**
         * �����и�ͼƬ
         * @param bitmap
         * @param x
         * @param y
         * @param w
         * @param h
         * @return
         */
        public Bitmap BitmapClipBitmap(Bitmap bitmap,int x, int y, int w, int h) {
            return  Bitmap.createBitmap(bitmap, x, y, w, h);
        }
        
        
	/**
	 * ��ȡ������Դ��ͼƬ
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap ReadBitMap(Context context, int resId) {
	    BitmapFactory.Options opt = new BitmapFactory.Options();
	    opt.inPreferredConfig = Bitmap.Config.RGB_565;
	    opt.inPurgeable = true;
	    opt.inInputShareable = true;
	    // ��ȡ��ԴͼƬ
	    InputStream is = context.getResources().openRawResource(resId);
	    return BitmapFactory.decodeStream(is, null, opt);
	}
	
	/**
	 * ���ƻ�����Ӱ������
	 * @param canvas
	 * @param str
	 * @param color
	 * @param x
	 * @param y
	 */
	public final void drawRimString(Canvas canvas, String str, int color,int x, int y) {
	    int backColor = mPaint.getColor();
	    mPaint.setColor(~color);
	    canvas.drawText(str, x + 1, y, mPaint);
	    canvas.drawText(str, x, y + 1, mPaint);
	    canvas.drawText(str, x - 1, y, mPaint);
	    canvas.drawText(str, x, y - 1, mPaint);
	    mPaint.setColor(color);
	    canvas.drawText(str, x, y, mPaint);
	    mPaint.setColor(backColor);
	}

	@Override
	public void run() {
	    while (mIsRunning) {
		try {
		    Thread.sleep(100);
		    // ˢ����Ļ
		    postInvalidate();
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	mAnimView.setKeyState(keyCode,true);
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
	mAnimView.setKeyState(keyCode,false);
        return super.onKeyUp(keyCode, event);
    }
}