package Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

class mainFrame extends JFrame implements Runnable, KeyListener
{
	private static final long serialVersionUID = 1L;
	Random r = new Random();
	Toolkit tk = Toolkit.getDefaultToolkit();
	Thread th;
	Image buffImage;
	Graphics buffg;

	Image title;
	Image point;
	Image pointNon;

	Image hero[] = new Image[2];
	Image heroShadow;
	Image heroDamaged;	
	Image live;
	Image monster[] = new Image[4];
	Image monsterLDeath[] = new Image[8];
	Image monsterRDeath[] = new Image[8];
	Image bossDeath[] = new Image[8];
	Image boss[] = new Image[2];
	Image bossBullet;
	Image bossLive;

	Image food;
	Image sword;
	Image swordAttack;
	Image swordAttacked;
	Image swordAttackAttacked;

	Image upper;
	Image bottom;

	Image Ending;
	Image backgroundBoss;
	Image backgroundSky;
	Image background;
	Image backgroundOrnaments[] = new Image[4];


	ArrayList<Enemy> Enemies = new ArrayList<>();
	ArrayList<Item> Items = new ArrayList<>();
	ArrayList<Bullet> Bullets = new ArrayList<>();

	Enemy en;
	Item it;
	Boss bs;
	Bullet bu;

	final int frameX = 1200;
	final int frameY = 860;
	int scene; // 0 : 타이틀, 1 : 플레이, 2 : 엔딩
	int menu;
	int level;

	int buttonX;
	int buttonY;

	int charX;
	int charY;
	int lives;
	int charStatus; // 0 : 평상시, 1 : 공격
	int score;
	int nx;

	double playerJumpMathVar;
	double attackDamage;

	int bossRandWalkY;
	int bossRandWalkX;
	int bossAttackNum;
	int bossAttackCool;
	int bossAttackStayTime;
	int bossSpecialAttackNum;
	int bossSpecialAttackCool;
	double calCntBulletSize;
	double bulletSize;
	int bulletWidth;
	int bulletHeight;

	boolean gameOver;
	boolean bossActive;
	boolean afterBossDie;
	boolean success;
	boolean successWav;
	boolean gameOverWav;
	boolean titleWav;
	boolean fightWav;
	boolean endWav;
	boolean changeScene;
	boolean gameOn;
	boolean onJumped;
	boolean onPause;
	boolean whileAttacked;
	boolean tutorial;

	int cnt;
	int cntBossBx;
	int cntBx;	
	int cntItem;
	int cntJump;
	int cntAttack;
	int cntAttackCool;
	int cntGameOver;
	int cntSuccess;
	int cntStartLevel;
	int cntEnd;
	int cntSceneChange;
	int cntSceneWait;
	int cntTutorial;
	int attackCoolTime;
	int walkNum;


	Clip[] clip = new Clip[1]; // 0배경
	Clip[] clips = new Clip[4]; // 0킬 1먹기 2보스컷

	boolean onSuccessionDamaged;
	boolean onDamaged;
	int damageCoolTime;
	int damageSuccessionCoolTime;


	boolean attackCool;

	int createMonTiming;
	int createItemTiming;

	int orNum;

	boolean keyUp, keyDown, keyRight, keyLeft, keyK, keyJ;

	mainFrame()
	{
		try
		{

			init();
			start();
			setTitle("NeNeQuest");
			setSize(frameX,frameY);

			Dimension screen = tk.getScreenSize();
			int f_xpos = (int)screen.getWidth()/2 - frameX/2;
			int f_ypos = (int)screen.getHeight()/2 - frameY/2;

			setLocation(f_xpos,f_ypos);
			setResizable(false);
			setVisible(true);

			r.setSeed(System.currentTimeMillis());
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void init()
	{

		buttonX = 330;
		buttonY = 600;

		charX = 500;
		charY = 400;
		lives = 100;
		score = 0;
		level = 0;

		scene = 0;
		menu = 0;

		cnt = 0;
		cntBx = 0;
		cntBossBx = 0;
		cntAttackCool = 0;
		cntAttack = 0;
		cntJump = 0;
		cntGameOver = 0;
		cntSuccess = 0;
		cntStartLevel = 0;
		cntEnd = 0;
		cntSceneChange = 0;
		cntSceneWait = 0;
		cntTutorial = 0;
		walkNum = 0;

		attackDamage = 1.0;
		attackCoolTime = 20;
		attackCool = true;
		bossActive = false;
		bossRandWalkY = 0;
		bossRandWalkX = -3;
		bossAttackNum = 0;
		bossSpecialAttackNum = 1;
		bossSpecialAttackCool = 0;
		bossAttackStayTime = 0;
		bossAttackCool = 0;
		createMonTiming = 34;
		createItemTiming = 150;
		calCntBulletSize = 0;
		bulletSize = 0;
		bulletWidth = 0;
		bulletHeight = 0;
		damageCoolTime = 0;
		damageSuccessionCoolTime = 0;
		nx = 6;
		charStatus = 0;
		orNum = r.nextInt(4);

		onDamaged = false;
		onSuccessionDamaged = false;
		gameOver = false;
		success = false;
		afterBossDie = false;
		bs = null;
		changeScene = false;
		titleWav = false;
		successWav = false;
		gameOverWav = false;
		fightWav = false;
		endWav = false;
		gameOn = false;
		onPause = false;
		whileAttacked = false;
		boolean tutorial = false;


		Bullets.removeAll(Bullets);
		Enemies.removeAll(Enemies);
		Items.removeAll(Items);

		for(int i=0; i<clips.length; i++)
		{
			clips[i] = null;
		}

		addSound("src/Sounds/Kill2.wav", false, 0);
		addSound("src/Sounds/Eat.wav", false, 1);
		addSound("src/Sounds/BossCut.wav", false, 2);
		addSound("src/Sounds/KillMob.wav", false, 3);


		for(int i=0; i<clip.length; i++)
		{
			clip[i] = null;
		}

		title= new ImageIcon("src/Images/Title.png").getImage();
		point= new ImageIcon("src/Images/Point.png").getImage();
		pointNon= new ImageIcon("src/Images/Point2.png").getImage();
		upper = new ImageIcon("src/Images/UpperSide.png").getImage();
		bottom = new ImageIcon("src/Images/DownSide.png").getImage();

		for(int i=0; i<monsterLDeath.length; i++)
			monsterLDeath[i] = new ImageIcon("src/Images/PigDeathL" + (i+1) + ".png").getImage();	

		for(int i=0; i<monsterRDeath.length; i++)
			monsterRDeath[i] = new ImageIcon("src/Images/PigDeathR" + (i+1) + ".png").getImage();	

		for(int i=0; i<bossDeath.length; i++)
			bossDeath[i] = new ImageIcon("src/Images/BossDeath" + (i+1) + ".png").getImage();	


		for(int i=0; i<hero.length; i++)
			hero[i] = new ImageIcon("src/Images/Hero" + (i+1) + ".png").getImage();
		live = new ImageIcon("src/Images/Helmet.png").getImage();
		sword = new ImageIcon("src/Images/Sword.png").getImage();
		swordAttack = new ImageIcon("src/Images/SwordAttack.png").getImage();
		swordAttacked = new ImageIcon("src/Images/Sword.png").getImage();
		swordAttackAttacked = new ImageIcon("src/Images/SwordAttackAttacked.png").getImage();
		heroShadow = new ImageIcon("src/Images/HeroShadow.png").getImage();
		heroDamaged = new ImageIcon("src/Images/HeroDamaged.png").getImage();

		for(int i=0; i<monster.length; i++)
			monster[i] = new ImageIcon("src/Images/Pig"+(i+1)+".png").getImage();

		for(int i=0; i<boss.length; i++)
			boss[i] = new ImageIcon("src/Images/Boss" + (i+1) + ".png").getImage();
		bossBullet = new ImageIcon("src/Images/BossBullet.png").getImage();
		bossLive = new ImageIcon("src/Images/BossHead.png").getImage();

		food = new ImageIcon("src/Images/Onigiri.png").getImage();

		Ending = new ImageIcon("src/Images/Ending.png").getImage();
		background = new ImageIcon("src/Images/Background.png").getImage();
		backgroundBoss = new ImageIcon("src/Images/BackgroundBossTheme.png").getImage();
		backgroundSky = new ImageIcon("src/Images/BackgroundSky.png").getImage();
		for(int i=0; i<backgroundOrnaments.length; i++)
			backgroundOrnaments[i] = new ImageIcon("src/Images/BackOrnament" + (i+1) + ".png").getImage();
	}


	private void start()
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		addKeyListener(this);
		th = new Thread(this);
		th.start();
	}


	public void run() {
		try
		{
			while(true){

				if(scene==1 && !onPause)
				{
					if(bossActive || afterBossDie)
						bossProcess();

					gameProcess();
					playerProcess();
					EnemyProcess();
					bulletProcess();
					ItemProcess();
				}

				if(scene==2)
				{
					endingProcess();
				}

				KeyProcess();
				repaint();
				Thread.sleep(19);
				cnt++;
				cntAttackCool++;
			}
		}
		catch(Exception e){e.printStackTrace();}
	}

	/**
	 * Draw
	 */

	public void paint(Graphics g)
	{
		buffImage = createImage(frameX,frameY);
		buffg = buffImage.getGraphics();
		update(g);
	}

	public void update(Graphics g)
	{
		drawBackground();

		switch(scene){
		case 0 :
			break;
		case 1 :
			drawItem();
			drawEnemy();
			drawPlayer();
			drawBullet();
			drawStatus();
			drawChangeScene();
			break;
		}

		g.drawImage(buffImage, 0, 0, this);
	}

	public void drawChangeScene()
	{

		if(changeScene)
		{
			buffg.drawImage(upper,0,cntSceneChange-340,this);
			buffg.drawImage(bottom,0,frameY-cntSceneChange,this);

			if(cntSceneWait==0){
				cntSceneChange += (nx*3) * Math.sin(Math.toRadians((double)(cntSceneChange+200)/640*90));
			}

			if(cntSceneChange >= 380)
			{
				cntSceneWait++;
				if(cntSceneWait >= 30)
				{
					nx = -nx;
					cntSceneWait = 0;
					if(gameOn){
						scene = 1;
						gameOn = false;
					}
				}
			}


			if(nx==-6 && cntSceneChange < 0)
			{
				nx = -nx;
				cntSceneChange = 0;
				changeScene = false;
				tutorial = true;
			}
		}		

		else
		{
			if(tutorial && !onPause){
				buffg.setColor(Color.BLACK);
				buffg.setFont(new Font("Default", Font.BOLD, 60));
				buffg.drawString("MOVE : ARROWS" , frameX/2-270, 300);
				buffg.drawString("JUMP : J / ATTACK : K" , frameX/2-320, 400);
				buffg.drawString("PAUSE : P / GO TO TITLE : ESC" , frameX/2-435, 500);
				cntTutorial++;
			}

			if(cntTutorial>200){
				cntTutorial = 0;
				tutorial = false;
			}
		}
	}


	public void drawBackground()
	{
		buffg.clearRect(0,0,frameX,frameY);

		switch(scene)
		{

		case 0 :
			buffg.drawImage(title, 0, 0, this);
			buffg.drawImage(point, buttonX, buttonY, this);

			if(!titleWav)
			{
				backSound("src/Sounds/NeNeStart.wav", false, 0);
				titleWav = true;
			}


			buffg.setFont(new Font("Default", Font.BOLD, 20));
			buffg.setColor(Color.BLACK);
			buffg.drawString("Ver : 1.2", 10, frameY-20);

			break;

		case 1 :

			if(!fightWav)
			{
				backSound("src/Sounds/NeNeFightWithAhagon.wav", true, 0);
				fightWav = true;
			}

			if(cntBossBx != 0)
				if(cnt%1200>0 && cnt%1200<800)
					orNum = -1;

			if(!bossActive){
				if(afterBossDie){
					if(cntBossBx >= -2400)
					{
						if(!onPause){
							cntBx -= 3;
							cntBossBx -= 3;
						}
						if(cntBx < -3300)
							cntBx = 0;
					}
					else
						afterBossDie = false;

					buffg.drawImage(backgroundSky, cntBx, 0, this);
					buffg.drawImage(background, cntBx, 295, this);
					buffg.drawImage(backgroundBoss, cntBossBx+frameX, 295, this);

					if(charX>cntBossBx+1400 && charX<cntBossBx+1800 && (charY < 150 || charY > 350))
					{
						charX-=3;
						if(charX<-180)
							lives -= 5;
					}
				}

				else{
					cntBossBx = 0;
					if(cntBx > -3300)
						if(!onPause){
							cntBx -= 3;
						}
						else{}

					else
						cntBx = 0;

					buffg.drawImage(backgroundSky, cntBx, 0, this);
					buffg.drawImage(background, cntBx, 295, this);

					if(cntBx%3300==0)
					{
						orNum = r.nextInt(4);

						if(cnt%1200>400 && cnt%1200<800)
							orNum = -1;
					}
				}

				switch(orNum)
				{
				case -1 :
					break;
				case 0:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,100,271,277,this);
					break;
				case 1:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,100,330,250,this);
					break;
				case 2:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,-200,545,600,this);
					break;
				case 3:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,-50,680,450,this);
					break;
				}
			}

			else{

				if(cntBossBx >= -1200)
				{
					if(!onPause){
						cntBx -= 3;

						if(cntBx < -3300)
							cntBx = 0;

						cntBossBx -= 3;
					}

					if(charX>cntBossBx+1400 && charX<cntBossBx+1800 && (charY < 150 || charY > 350))
					{
						charX-=3;
						if(charX<-180)
							lives -= 5;
					}	
				}

				buffg.drawImage(backgroundSky, cntBx, 0, this);
				buffg.drawImage(background, cntBx, 295, this);
				buffg.drawImage(backgroundBoss, cntBossBx+frameX, 295, this);

				switch(orNum)
				{
				case -1 :
					break;
				case 0:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,100,271,277,this);
					break;
				case 1:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,100,330,250,this);
					break;
				case 2:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,-200,545,600,this);
					break;
				case 3:
					buffg.drawImage(backgroundOrnaments[orNum],cntBx+1400,-50,680,450,this);
					break;
				}
			}

			break;

		case 2 :

			buffg.drawImage(Ending, 0, -cntEnd, this);
			buffg.setFont(new Font("Default", Font.BOLD, 20));
			buffg.setColor(Color.WHITE);
			buffg.drawString("CLOSE : ESC", frameX-200, 100);

			buffg.setFont(new Font("Default", Font.BOLD, 100));
			buffg.drawString("NENE QUEST", frameX/2-300, 800-cntEnd);
			buffg.setFont(new Font("Default", Font.PLAIN, 30));
			buffg.drawString("Ver 1.2", frameX/2-200, 900-cntEnd);
			buffg.drawString("PROGRAMMING : ANYANG" , frameX/2-300, 1000-cntEnd);
			buffg.drawString("SOUND : ANYANG" , frameX/2-300, 1100-cntEnd);
			buffg.drawString("MUSIC : ANYANG" , frameX/2-300, 1200-cntEnd);
			buffg.drawString("GRAPHIC : NENE SAKURA & ANYANG" , frameX/2-300, 1300-cntEnd);
			buffg.drawString("IDEA : NENE SAKURA" , frameX/2-300, 1400-cntEnd);
			buffg.drawString("ORIGINAL : NEW GAME \"NENE QUEST\"" , frameX/2-300, 1500-cntEnd);
			buffg.drawString("HOME PAGE : fool8474.blog.me" , frameX/2-300, 1700-cntEnd);			

			buffg.setFont(new Font("Default", Font.BOLD, 60));
			buffg.drawString("SCORE : " + score , frameX/2-200, 2400-cntEnd);
			buffg.drawString("THANK YOU FOR PLAYING." , frameX/2-400, 2500-cntEnd);


			break;
		}

		if(gameOn){
			drawChangeScene();
		}
	}

	public void drawBullet()
	{
		for(int i=0; i<Bullets.size(); i++)
		{
			bu = (Bullet)Bullets.get(i);

			switch(bu.type)
			{

			case 1 :
				buffg.drawImage(bossBullet, bu.x, bu.y, this);
				break;

			case 2 :

				calCntBulletSize = bu.cntBullet;

				if(calCntBulletSize>=100)
					calCntBulletSize = 100;
				else if(calCntBulletSize<=20)
					calCntBulletSize = 20;

				bulletSize = (Math.sin(Math.toRadians(calCntBulletSize*1.5)));
				bulletWidth = (int)(bossBullet.getWidth(null)*bulletSize/1.5);
				bulletHeight = (int)(bossBullet.getHeight(null)*bulletSize/1.5);


				buffg.drawImage(bossBullet, bu.x, bu.y, bulletWidth, bulletHeight,this);

				break;
			}
		}
	}

	public void drawEnemy()
	{

		for(int i=0; i<Enemies.size(); i++)
		{
			en = (Enemy)Enemies.get(i);

			if(en.live){
				if(en.frontBehind){
					if(en.walkNum>=0 && en.walkNum < 5)
					{
						buffg.drawImage(monster[0], en.x, en.y, this);
					}
					if(en.walkNum>=5 && en.walkNum <= 10)
					{
						buffg.drawImage(monster[1], en.x, en.y, this);
					}
				}
				else {
					if(en.walkNum>=0 && en.walkNum < 5)
					{
						buffg.drawImage(monster[2], en.x, en.y, this);
					}
					if(en.walkNum>=5 && en.walkNum <= 10)
					{
						buffg.drawImage(monster[3], en.x, en.y, this);
					}
				}
			}

			else{
				if(en.frontBehind){
					if(en.deathNum<22)
					{
						buffg.drawImage(monsterLDeath[en.deathNum/3], en.x, en.y, this);							
					}
				}
				else
				{
					if(en.deathNum<22)
					{
						buffg.drawImage(monsterRDeath[en.deathNum/3], en.x, en.y, this);
					}
				}
			}
		}

		if(bs!=null)
		{
			if(bs.live){
				if(whileAttacked)
				{
					whileAttacked = false;
				}

				else if(bs.bossState == 1){
					buffg.drawImage(boss[1], bs.x, bs.y, this);
				}

				else{
					buffg.drawImage(boss[0], bs.x, bs.y, this);
				}
			}

			else
			{
				buffg.drawImage(bossDeath[bs.deathNum/6], bs.x, bs.y, this);
			}

		}
	}

	public void drawItem()
	{
		for(int i=0; i<Items.size(); i++)
		{
			it = (Item)Items.get(i);

			buffg.drawImage(food, it.x, it.y,100,100,this);
		}
	}

	public void drawPlayer()
	{

		if(!onJumped)
		{

			buffg.drawImage(heroShadow, charX, charY+280, this);

			if((onDamaged || onSuccessionDamaged) && cnt%15 > 9){
				if(walkNum>=0 && walkNum<10)
					buffg.drawImage(heroDamaged, charX, charY, this);
				else
					buffg.drawImage(heroDamaged, charX, charY, this);
			}

			else
			{
				if(walkNum>=0 && walkNum<10)
					buffg.drawImage(hero[0], charX, charY, this);
				else
					buffg.drawImage(hero[1], charX, charY, this);
			}

			if(charStatus==0)
			{
				if((onDamaged || onSuccessionDamaged) && cnt%15 > 9)
					buffg.drawImage(swordAttacked, charX+140, charY-80, this);

				else
					buffg.drawImage(sword, charX+140, charY-80, this);
			}

			else if(charStatus==1)
			{
				if((onDamaged || onSuccessionDamaged) && cnt%15 > 9)
					buffg.drawImage(swordAttackAttacked, charX+160, charY+20, this);
				else
					buffg.drawImage(swordAttack, charX+160, charY+20, this);
			}
		}

		else
		{
			double playerJumpMathVarCanResize = playerJumpMathVar;

			if(playerJumpMathVarCanResize > 0.5)
				playerJumpMathVarCanResize = 0.5;


			buffg.drawImage(heroShadow, charX + (int)(0.5*heroShadow.getWidth(null)*(playerJumpMathVarCanResize)), 
					charY+280+(int)(0.5*heroShadow.getHeight(null)*(playerJumpMathVarCanResize)), 
					(int)(heroShadow.getWidth(null)*(1-playerJumpMathVarCanResize)), 
					(int)(heroShadow.getHeight(null)*(1-playerJumpMathVarCanResize)), this);

			if((onDamaged || onSuccessionDamaged) && cnt%15 > 9) {
				buffg.drawImage(heroDamaged, charX, charY-(Math.abs((int)(200*playerJumpMathVar))), this);
				buffg.drawImage(swordAttacked, charX+140, charY-80-(Math.abs((int)(200*playerJumpMathVar))), this);
			}

			else {
				buffg.drawImage(hero[0], charX, charY-(Math.abs((int)(200*playerJumpMathVar))), this);
				buffg.drawImage(sword, charX+140, charY-80-(Math.abs((int)(200*playerJumpMathVar))), this);
			}
		}
	}

	public void drawStatus()
	{
		buffg.setFont(new Font("Default", Font.BOLD, 40));
		buffg.drawString("score : " + score, 500, 100);


		if(success)
		{
			buffg.setColor(Color.blue);
			buffg.setFont(new Font("Default", Font.ITALIC, 120));
			buffg.drawString("GAME CLEAR", frameX/2-400, frameY/2);
		}

		else if(!success)
		{
			buffg.setFont(new Font("Default", Font.BOLD, 20));
			buffg.drawString("Level : " + (level+1), 500, 120);
		}

		if(onPause)
		{
			buffg.setColor(Color.black);
			buffg.setFont(new Font("Default", Font.PLAIN, 120));
			buffg.drawString("PAUSE", frameX/2-200, frameY/2);
		}

		if(gameOver)
		{
			buffg.setColor(Color.black);
			buffg.setFont(new Font("Default", Font.ITALIC, 120));
			buffg.drawString("GAME OVER", frameX/2-400, frameY/2);
		}

		if(lives>0)
		{
			buffg.setColor(new Color((int)((double)(100-lives)/100*255), (int)((double)lives/100*255), 0));
		}



		buffg.fillRect(60, 70, (350*(lives))/100, 30);
		buffg.setColor(Color.black);
		buffg.drawRect(60, 65, 355, 40);
		buffg.drawImage(live, 30, 50, 60,60,this);

		if(bs != null)
		{
			buffg.setColor(new Color(255, 180-(int)((bs.initialHp-bs.hp)/bs.initialHp*180),0));
			buffg.fillRect(785+(int)(350*((double)(bs.initialHp-bs.hp)/bs.initialHp)), 70, (int)(350*((double)bs.hp/bs.initialHp)), 30);
			buffg.setColor(Color.black);
			buffg.drawRect(780, 65, 355, 40);
			buffg.drawImage(bossLive, 1100, 50, 60,60,this);
		}


	}


	/**
	 * Process
	 */

	public void damageProcess(int type, int damage)
	{
		switch(type)
		{
		case 1://한번 하고 사라지는타입

			if(!onDamaged){
				lives -= damage;
				onDamaged = true;
			}
			break;

		case 2://연속적
			if(!onSuccessionDamaged){
				lives -= damage;
				onSuccessionDamaged = true;
			}
			break;
		}
	}

	public void gameProcess()
	{

		for(int i=0; i<clips.length; i++)
		{
			if(clips[i].getFrameLength() == clips[i].getFramePosition())
				clips[i].setFramePosition(0);
		}

		if(bs==null){
			score++;
		}

		if(level == 5)
		{
			success = true;
			cntSuccess++;
			if(!successWav)
			{
				backSound("src/Sounds/Success.wav", false, 0);
				successWav = true;
			}
		}

		if(lives < 0)
		{
			gameOver = true;
			cntGameOver++;
			if(!gameOverWav)
			{
				backSound("src/Sounds/GameOver.wav", false, 0);
				gameOverWav = true;
			}
		}

		if(cntSuccess>400)
		{
			scene = 2;
		}

		if(cntGameOver>400)
		{
			scene = 0;
			init();
		}

		if(!afterBossDie)
			cntStartLevel++;

		if(cnt%500<1 && cntStartLevel>700)
			bossActive = true;

		if(onSuccessionDamaged)
		{
			damageSuccessionCoolTime++;

			if(damageSuccessionCoolTime>=3)
			{
				onSuccessionDamaged = false;
				damageSuccessionCoolTime = 0;
			}
		}

		if(onDamaged)
		{
			damageCoolTime++;

			if(damageCoolTime>=50)
			{
				onDamaged = false;
				damageCoolTime = 0;

			}
		}

		if(keyJ && !onJumped)
		{
			onJumped = true;
		}
	}

	public void endingProcess()
	{

		if(!endWav){
			backSound("src/Sounds/StepByStepVerAnyang.wav",true,0);
			endWav = true;
		}

		if(cntEnd<=2000){
			cntEnd++;
		}
	}

	public void bulletProcess()
	{
		for(int i=0; i<Bullets.size(); i++)
		{
			bu = (Bullet)Bullets.get(i);

			switch(bu.type)
			{
			case 1 :
				bu.move(-3, 3);

				if(CrashWithXY(charX+50, charY+287, bu.x, bu.y+150, hero[0].getWidth(null)-90, 40, bossBullet.getWidth(null), bossBullet.getHeight(null)-100))
				{
					if(!gameOver)
						damageProcess(1, bu.damage);

					Bullets.remove(i);
				}

				else if(keyJ && CrashWithXY(charX+50, charY+247-(Math.abs((int)(200*playerJumpMathVar))), bu.x, bu.y+150, hero[0].getWidth(null)-90, 40, bossBullet.getWidth(null), bossBullet.getHeight(null)-100))
				{
					if(!gameOver)
						damageProcess(1, bu.damage);

					Bullets.remove(i);
				}

				else if(bu.x<0 || bu.y > frameY)
					Bullets.remove(i);

				break;

			case 2 :

				bu.bulletUse(1);
				if(bu.cntBullet>=0 && bu.cntBullet<=140)
				{
					if(bu.cntBullet>50){
						bu.bulletUse(3);
					}

					bu.move(-r.nextInt(3)-4, -bu.moveSource*(Math.cos(Math.toRadians(bu.cntBullet*1.5))));



					if(CrashWithXY(charX+50, charY+287, bu.x, bu.y, hero[0].getWidth(null)-90, 40, bulletWidth, bulletHeight))
					{
						if(!gameOver)
							damageProcess(2, bu.damage);
					}

					else if(keyJ && CrashWithXY(charX+50, charY+247-(Math.abs((int)(200*playerJumpMathVar))), bu.x, bu.y, hero[0].getWidth(null)-90, 40, bulletWidth, bulletHeight))
					{
						if(!gameOver)
							damageProcess(2, bu.damage);
					}
				}
				else
				{
					Bullets.remove(i);
				}
				break;

			default :
				break;
			}
		}
	}

	public void bossProcess()
	{
		if(bossActive)
		{
			if(bs==null)
			{
				if(cntBossBx<=-1200){
					bs = new Boss(frameX,frameY/2-300,300+level*150);
				}
			}

			else
			{
				if(bs.live)
				{
					if(charStatus==1 && (CrashWithXY(charX+170, charY+150, bs.x, bs.y+100, swordAttack.getWidth(null), 10, boss[0].getWidth(null)/3, boss[0].getHeight(null)/4)
							|| CrashWithXY(charX+170, charY+150, bs.x+100, bs.y+400, swordAttack.getWidth(null), 10, boss[0].getWidth(null)-100, boss[0].getHeight(null)-520)))		
					{
						if(!gameOver){
							bs.hp -= attackDamage;

							clips[0].start();

							whileAttacked = true;
						}
					}

					if(CrashWithXY(charX, charY+287, bs.x, bs.y+100, hero[0].getWidth(null)-100, 40, boss[0].getWidth(null)/3, boss[0].getHeight(null)/4)
							|| CrashWithXY(charX, charY+287, bs.x+100, bs.y+400, hero[0].getWidth(null)-100, 40, boss[0].getWidth(null)-100, boss[0].getHeight(null)-520))
					{

						if(!gameOver){
							damageProcess(1, 25);
						}
					}

					else if(keyJ && CrashWithXY(charX, charY+247-(Math.abs((int)(200*playerJumpMathVar))), bs.x, bs.y+100, hero[0].getWidth(null)-100, 40, boss[0].getWidth(null)/3, boss[0].getHeight(null)/4)
							|| CrashWithXY(charX, charY+247-(Math.abs((int)(200*playerJumpMathVar))), bs.x+100, bs.y+400, hero[0].getWidth(null)-100, 40, boss[0].getWidth(null)-100, boss[0].getHeight(null)-520))
					{
						if(!gameOver){
							damageProcess(1, 25);
						}
					}

					bs.bossActive();
					bossAttackNum = bs.bossCnt%(600-level*60);
					bossAttackCool = bs.bossCnt%(30-level*4);
					bossSpecialAttackNum = bs.bossCnt%(1000-level*100);
					bossSpecialAttackCool = bs.bossCnt%3;

					if(bossSpecialAttackNum == 0 && bs.bossState!=2)
					{
						bs.bossState = 3;
					}

					if(bossAttackNum == 0 && bs.bossState!=3)
					{
						bs.bossState = 2;
					}



					switch(bs.bossState)
					{
					case 1 :
						int walkNum = bs.bossCnt%320;

						if(walkNum == 80 || walkNum == 240)
						{
							bossRandWalkY = r.nextInt(3)-1;
						}

						if(bs.y<-100)
							bossRandWalkY = 1;
						else if(bs.y>250)
							bossRandWalkY = -1;

						if(walkNum == 0 || walkNum == 160)
							bossRandWalkX = -bossRandWalkX;

						if(bs.x>frameX-300)
						{
							bossRandWalkX = -3;
							bs.move(bossRandWalkX, 0);
						}

						else if(bs.x<600)
						{
							bossRandWalkX = 3;
							bs.move(bossRandWalkX, 0);
						}

						if(walkNum>=0 && walkNum<=80)
						{
							bs.move(bossRandWalkX, bossRandWalkY);
						}

						else if(walkNum>=160 && walkNum<=240)
						{
							bs.move(bossRandWalkX, bossRandWalkY);
						}

						//bossMove
						break;

					case 2 :
						bossAttackStayTime++;

						if(bossAttackStayTime>15){
							if(bs.attackCount<5)
							{
								if(bossAttackCool==0)
								{
									bu = new Bullet(bs.x,bs.y,10,1);
									Bullets.add(bu);
									bs.attackCount++;
								}
							}


							else
							{
								//bs.bossState = 3;
								bs.bossState = 1;
								bs.attackCount = 0;
								bossAttackStayTime = 0;
							}
						}
						//bossAttack
						break;

					case 3 :

						bossAttackStayTime++;

						if(bossAttackStayTime>100){
							if(bs.attackCount<100)
							{
								if(bossSpecialAttackCool==0)
								{
									bu = new Bullet(bs.x-60,bs.y+180,2,2,-(r.nextInt(7+(level*2))+4));
									Bullets.add(bu);
									bs.attackCount++;
								}
							}
							else
							{
								bs.bossState = 1;
								bs.attackCount = 0;
								bossAttackStayTime = 0;
							}
						}

						else
							if(bossAttackStayTime%2==0)
								bs.y += 7;

							else
								bs.y -= 7;

						break;
					}

					if(bs.hp <= 0)
					{
						afterBossDie = true;
						cnt = 0;
						cntStartLevel = 0;
						score += (500 + level*250);
						level++;
						bs.live = false;
					}
				}
				else
				{
					bs.death();
					if(bs.deathNum>=43)
					{
						bossActive = false;
						bs = null;
						clips[2].start();
					}
				}
			}
		}
	}


	public void EnemyProcess()
	{
		if(!bossActive && !afterBossDie){
			if(cnt%(createMonTiming-level*3) == 0)
			{
				boolean FB = r.nextBoolean();

				if(FB)
				{
					en = new Enemy(frameX + 100, r.nextInt(frameY-350)+250, FB);
				}

				else
				{
					en = new Enemy(-100, r.nextInt(frameY-350)+250, FB);
				}

				Enemies.add(en);
			}
		}

		for(int i=0; i<Enemies.size(); i++)
		{
			en = (Enemy)Enemies.get(i);

			if(en.live){
				if(en.frontBehind) en.move(-20-level*2,0);
				else en.move(10+level, 0);

				en.walking();

				if(en.x<-150 || en.y<-115 || en.y>frameY+115 || en.x>frameX+200)
				{
					Enemies.remove(i);
				}

				else if(charStatus==1 && CrashWithXY(charX+170, charY+150, en.x, en.y, swordAttack.getWidth(null), 10, monster[0].getWidth(null), monster[0].getHeight(null)))
				{
					if(!gameOver)
					{
						score += (50 + level*10);
					}
					en.live = false;

				}


				else if(charStatus!=2 && CrashWithXY(charX, charY+287, en.x, en.y+100, hero[0].getWidth(null)-40, 40, monster[0].getWidth(null), 75))
				{
					if(!gameOver)
					{
						score += (50 + level*10);
					}
					damageProcess(1, 15);
					en.live = false;
				}
			}

			else{
				en.death();
				if(en.deathNum>=23){
					Enemies.remove(i);
					clips[3].start();
				}
			}
		}
	}

	public void ItemProcess()
	{
		cntItem++;
		createItemTiming=100-level*5;

		if(!bossActive && !afterBossDie){
			if(cntItem%createItemTiming==0)
			{
				Item newIt = new Item(frameX + 100, r.nextInt(frameY-380)+240);
				Items.add(newIt);
				cntItem = 0;
			}
		}

		for(int i=0; i<Items.size(); i++)
		{
			it = (Item)Items.get(i);

			it.move(-30,0);

			if(it.x<-150 || it.y<-115 || it.y>frameY+115)
			{
				Items.remove(i);
			}

			if(charStatus!=2&&CrashWithXY(charX, charY+287, it.x, it.y, hero[0].getWidth(null)-40, 40, 100, 100))
			{
				Items.remove(i);

				if(!gameOver){
					if(lives<85){
						lives+=15;
					}

					else
					{
						lives=100;
					}
				}

				clips[1].start();
			}
		}
	}

	void playerProcess()
	{
		if(!onJumped)
		{
			walkNum = cnt%20;
		}

		else
		{
			cntJump += 4;

			playerJumpMathVar = Math.abs((Math.sin(Math.toRadians(cntJump))));

			if(cntJump >=180)
			{	
				cntJump = 0;
				onJumped = false;
				keyJ = false;
			}
		}
	}

	private void getAttackTiming()
	{
		cntAttack++;

		if(attackCoolTime <= cntAttackCool && !attackCool)
		{
			cntAttackCool = 0;
			attackCool = true;
		}

		if((cntAttack%attackCoolTime>=0 && cntAttack%attackCoolTime<=10) 
				&& attackCool == true && keyK)
		{	
			charStatus = 1;// 0 : 평상시, 1 : 공격
		}

		else
		{
			charStatus = 0;// 0 : 평상시, 1 : 공격
		}
	}

	private void getJumpTiming()
	{
		if(cntJump!=0){
			charStatus = 2;
		}

		if(cntJump==180){
			keyJ = false;
			charStatus = 0;
			cntJump = 0;
		}

	}

	public void KeyProcess()
	{
		switch(scene){
		case 0 :
			break;
		case 1 :
			if(!bossActive && !afterBossDie){
				if(keyUp){if(charY < 20) {} else charY -= 5;}
				if(keyDown){if(charY > frameY - 350) {} else charY += 5;}
				if(keyRight){if(charX > frameX - 250) {} else charX += 5;}
				if(keyLeft){if(charX < 25) {} else charX -= 5;}
			}

			else if(bossActive || afterBossDie){
				if(keyUp){if(charY < 20) {} else if(charX > cntBossBx+1450 && charX < cntBossBx+1750 && charY<160) {} else charY -= 5;}
				if(keyDown){if(charY > frameY - 350) {} else if(charX > cntBossBx+1450 && charX < cntBossBx+1750 && charY>320) {} else charY += 5;}
				if(keyRight){if(charX > frameX - 250) {} else if(charX > cntBossBx+1400 && (charY > 350 || charY < 150) && !(charX>cntBossBx+1600)) {} else charX += 5;}
				if(keyLeft){if(charX < 25) {} else if(charX < cntBossBx+1850 && (charY > 350 || charY < 150) && charX>cntBossBx+1600) {} else charX -= 5;}
			}

			if(!attackCool)
				cntAttackCool++;

			getAttackTiming();
			getJumpTiming();
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(scene)
		{
		case 0:
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_UP:
				if(menu>0){
					buttonY -= 73;
					menu--;
				}
				break;

			case KeyEvent.VK_DOWN:
				if(menu<2){
					buttonY += 73;
					menu++;
				}
				break;

			case KeyEvent.VK_ENTER : case KeyEvent.VK_J: case KeyEvent.VK_K:
				if(menu==0)
				{
					changeScene = true;
					gameOn = true;
				}

				else if(menu==1)
				{}

				else if(menu==2)
				{
					System.exit(0);
				}
			}

			break;

		case 1:

			if(!onPause){
				switch(e.getKeyCode())
				{

				case KeyEvent.VK_UP:
					keyUp = true;
					break;

				case KeyEvent.VK_DOWN:
					keyDown = true;
					break;

				case KeyEvent.VK_RIGHT:
					keyRight = true;
					break;

				case KeyEvent.VK_LEFT:
					keyLeft = true;
					break;

				case KeyEvent.VK_K:
					keyK = true;
					break;

				case KeyEvent.VK_J:
					keyJ = true;
					break;

				case KeyEvent.VK_ESCAPE :
					clip[0].close();
					init();
					scene = 0;
					break;

				case KeyEvent.VK_P :
					onPause = true;
					break;
				}		
			}

			else
			{
				switch(e.getKeyCode()){

				case KeyEvent.VK_P :
					onPause = false;
					break;

				case KeyEvent.VK_ESCAPE :
					clip[0].close();
					init();
					scene = 0;
					onPause = false;
					break;
				}
			}

			break;


		case 2 :
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_ESCAPE:
				init();
				System.exit(0);

				break;
			}

			break;
		}




	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			keyUp = false;
			break;

		case KeyEvent.VK_DOWN:
			keyDown = false;
			break;

		case KeyEvent.VK_RIGHT:
			keyRight = false;
			break;

		case KeyEvent.VK_LEFT:
			keyLeft = false;
			break;

		case KeyEvent.VK_K:
			keyK = false;
			attackCool = false;
			cntAttack = 0;
			break;

		case KeyEvent.VK_J:
			keyJ = false;
			break;

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}	

	boolean CrashWithImage(int img1X, int img1Y, int img2X, int img2Y, Image img1, Image img2)
	{
		boolean result;

		if((Math.abs((img1X + img1.getWidth(null)/2) - (img2X + img2.getWidth(null)/2)) < 
				img2.getWidth(null)/2 + img1.getWidth(null)/2) &&
				(Math.abs((img1Y + img1.getHeight(null)/2) - (img2Y + img2.getHeight(null)/2)) < 
						img2.getHeight(null)/2 + img2.getHeight(null)/2))
			result = true;

		else
			result = false;

		return result;
	}

	boolean CrashWithXY(int img1X, int img1Y, int img2X, int img2Y, int img1Width, int img1Height, int img2Width, int img2Height)
	{
		boolean result;

		if((Math.abs((img1X + img1Width/2) - (img2X + img2Width/2)) < img2Width/2 + img1Width/2) &&
				(Math.abs((img1Y + img1Height/2) - (img2Y + img2Height/2)) < 
						img2Height/2 + img2Height/2))
			result = true;

		else
			result = false;

		return result;
	}

	void addSound(String file, boolean Loop, int n)
	{
		try{

			AudioInputStream audi = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));

			clips[n] = AudioSystem.getClip();
			clips[n].open(audi);

			if(Loop)clips[n].loop(-1);
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}

	void backSound(String file, boolean Loop, int i)
	{
		try{

			if(clip[i]!=null){
				if(clip[i].isRunning()){}
				clip[i].close();
			}

			AudioInputStream audi = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));

			for(int j=0; j<clip.length; j++)
			{
				if(clip[j]!=null)
				{
					if(clip[0] != null && (scene==2 && clip[0].isActive()))
					{
						clip[0].close();
					}
				}
			}

			clip[i] = AudioSystem.getClip();
			clip[i].open(audi);
			clip[i].start();
			if(Loop)clip[i].loop(-1);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
