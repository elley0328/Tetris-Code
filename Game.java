package Jh_Tetris;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;//Graphics 클래스는 지오메트리, 좌표변화, 컬러 관리및 배치에 대해 제어를 실시
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game extends Applet implements Runnable, ActionListener {

	Thread clock;
	
	Image off;
	Graphics offG;
	
	Random r;
	
	boolean[][] map;
	Color[] colorType;
	Color[][] colorMap;
	
	int blockType;
	static int[] blockX;
	static int[] blockY;
	int blockPos;
	
	int score;
	int delaytime;
	int runGame;
	
	AudioClip turnAudio;
	AudioClip deleteAudio;
	AudioClip gameOverAudio;
	
	Button startButton;
	Panel buttonPanel;
	
	public void init(){
		off=createImage(181,316);
		offG=off.getGraphics();
		offG.setColor(Color.white);
		offG.fillRect(0, 0, 192, 192);
		
		turnAudio=getAudioClip(getCodeBase(),"turn.au");
		deleteAudio=getAudioClip(getCodeBase(),"delete.au");
		gameOverAudio=getAudioClip(getCodeBase(),"gameover.au");
		
		setLayout(new BorderLayout());
		buttonPanel=new Panel();
		
		startButton=new Button("START");
		startButton.addActionListener(this);
		buttonPanel.add(startButton);
		add("South",buttonPanel);
		
		map=new boolean[12][21];
		colorMap=new Color[12][21];
		colorType=new Color[7];
		setColorType();
		
		blockX=new int[4];
		blockY=new int[4];
		blockPos=0;
		
		r=new Random();
		blockType=Math.abs(r.nextInt()%7);
		setBlockXY(blockType);
		drawBlock();
		drawMap();
		drawGrid();
		
		score = 0;
		delaytime = 1000;
		runGame = 0;
		
		addKeyListener(new MyKeyHandler());
	}
	public void drawGrid() {
		// TODO Auto-generated method stub
		offG.setColor(new Color(190,190,190));
		for(int i=0;i<12;i++){
			for(int j=0;j<21;j++){
				offG.drawRect(i*15, j*15, 15, 15);
			}
		}
	}
	public void drawMap() {
		// TODO Auto-generated method stub
		for(int i=0;i<12;i++){
			for(int j=0;j<21;j++){
				if(map[i][j]){
					offG.setColor(colorMap[i][j]);
					offG.fillRect(i*15, j*15, 15, 15);
				}
				else{
					offG.setColor(Color.white);
					offG.fillRect(i*15, j*15, 15, 15);
				}
			}
		}
	}
	public void drawBlock() {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++){
			map[blockX[i]][blockY[i]]=true;
			colorMap[blockX[i]][blockY[i]]=colorType[blockType];
		}
	}
	public void setBlockXY(int Type) {
		// TODO Auto-generated method stub
		switch(Type){
		case 0:
			blockX[0]=5;blockY[0]=0;
			blockX[1]=6;blockY[1]=0;
			blockX[2]=5;blockY[2]=1;
			blockX[3]=6;blockY[3]=1;
			break;
		case 1:
			blockX[0]=6;blockY[0]=0;
			blockX[1]=5;blockY[1]=1;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=7;blockY[3]=1;
			break;
		case 2:
			blockX[0]=7;blockY[0]=0;
			blockX[1]=5;blockY[1]=1;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=7;blockY[3]=1;
			break;
		case 3:
			blockX[0]=5;blockY[0]=0;
			blockX[1]=5;blockY[1]=1;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=7;blockY[3]=1;
			break;
		case 4:
			blockX[0]=5;blockY[0]=0;
			blockX[1]=5;blockY[1]=1;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=6;blockY[3]=2;
			break;
		case 5:
			blockX[0]=6;blockY[0]=0;
			blockX[1]=5;blockY[1]=1;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=5;blockY[3]=2;
			break;
		case 6:
			blockX[0]=4;blockY[0]=0;
			blockX[1]=5;blockY[1]=0;
			blockX[2]=6;blockY[2]=1;
			blockX[3]=7;blockY[3]=1;
			break;
		}
		
	}
	public void setColorType() {
		// TODO Auto-generated method stub 
		colorType[0]=new Color(255,113,113);
		colorType[1]=new Color(255,187,0);
		colorType[2]=new Color(255,228,0);
		colorType[3]=new Color(171,242,0);
		colorType[4]=new Color(0,216,255);
		colorType[5]=new Color(54,138,255);
		colorType[6]=new Color(185,90,255);
	}//block color setting
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		blockPos=0;
		for(int i=0;i<12;i++){
			for(int j=0;j<21;j++){
				map[i][j]=false;
			}
		}
		r=new Random();
		blockType=Math.abs(r.nextInt()%7);
		setBlockXY(blockType);
		
		drawBlock();
		drawMap();
		drawGrid();
		
		score=0;
		delaytime=1000;
		runGame=1;
		this.requestFocus();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				clock.sleep(delaytime);
			}catch(InterruptedException ie){}
			
			dropBlock();
			
			switch(runGame){
			case 1:
				drawBlock();
				drawMap();
				drawGrid();
				break;
			case 2:
				drawScore();
				break;
			default:
				drawTitle();
				break;
			}
			repaint();
		}
	}
	
	public void drawTitle() {
		// TODO Auto-generated method stub
		offG.setColor(Color.white);
		offG.fillRect(29, 120, 123, 70);
		offG.setColor(Color.black);
		offG.drawRect(31, 125, 121, 60);
		offG.setColor(Color.red);
		offG.drawString("★JH TETRIS★",51, 150);
		offG.setColor(Color.black);
		offG.drawString("Press START Button", 35, 170);
	}//game title window
	public void drawScore() {
		// TODO Auto-generated method stub
		offG.setColor(Color.white);
		offG.fillRect(35,120,110,70);
		offG.setColor(Color.black);
		offG.fillRect(40, 125, 100, 60);
		offG.setColor(Color.red);
		offG.drawString("Game Over!", 56, 150);
		offG.setColor(Color.blue);
		offG.drawString("Score : "+score, 56, 170);
	}//game score window
	public void dropBlock() {
		// TODO Auto-generated method stub
		removeBlock();
		if(checkDrop()){
			for(int i=0;i<4;i++){
				blockY[i]=blockY[i]+1;			
			}
		}
		else{
			drawBlock();
			nextBlock();
		}
	}
	public void nextBlock() {
		// TODO Auto-generated method stub
		blockType=Math.abs(r.nextInt()%7);
		blockPos=0;
		delLine();
		setBlockXY(blockType);
		checkGameOver();
	}
	public void checkGameOver() {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++){
			if(map[blockX[i]][blockY[i]]){
				if(runGame==1){
					gameOverAudio.play();
					runGame=2;
				}
			}
		}
	}
	public boolean checkDrop() {
		// TODO Auto-generated method stub
		boolean dropOK=true;
		for(int i=0;i<4;i++){
			if((blockY[i]+1)!=21){
				if(map[blockX[i]][blockY[i]+1])dropOK=false;
			}
			else{
				dropOK=false;
			}
		}
		return dropOK;
	}
	public void removeBlock() {
		// TODO Auto-generated method stub
		for(int i=0;i<4;i++){
			map[blockX[i]][blockY[i]]=false;
			colorMap[blockX[i]][blockY[i]]=Color.white;
		}
	}
	public void start(){
		if(clock==null){
			clock=new Thread(this);
			clock.start();
		}
	}
	
	public void paint(Graphics g){
		g.drawImage(off, 0, 0,this);
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void delLine(){
		boolean delOK;
		for(int row=20;row>=0;row--){
			delOK=true;
			for(int col=0;col<12;col++){
				if(!map[col][row])delOK=false;
			}
			if(delOK){
				deleteAudio.play();
				score+=10;
				
				if(score<1000){
					delaytime=1000-score;
				}else{
					delaytime=0;
				}
				for(int delRow=row;delRow>0;delRow--){
					for(int delCol=0;delCol<12;delCol++){
						map[delCol][delRow]=map[delCol][delRow-1];
						colorMap[delCol][delRow]=colorMap[delCol][delRow-1];
					}
				}
				for(int i=0;i<12;i++){
					map[0][i]=false;
					colorMap[0][i]=Color.white;
				}
				row++;
			}
		}
	}
	public void stop(){
		if((clock!=null)&&(clock.isAlive())){
			clock=null;
		}
	}
	class MyKeyHandler extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {//키보드를 이용해서 블록제어
			// TODO Auto-generated method stub
			int keyCode=(int)e.getKeyCode();
			if(keyCode==KeyEvent.VK_LEFT){//왼쪽 방향키
				if(checkMove(-1)){
					for(int i=0;i<4;i++){
						blockX[i]=blockX[i]-1;
					}
				}
			}
			if(keyCode==KeyEvent.VK_RIGHT){//오른쪽 방향키
				if(checkMove(1)){
					for(int i=0;i<4;i++){
						blockX[i]=blockX[i]+1;
					}
				}
			}
			if(keyCode==KeyEvent.VK_DOWN){//아래 방향키
				removeBlock();
				if(checkDrop()){
					for(int i=0;i<4;i++){
						blockY[i]=blockY[i]+1;
					}
				}
				else{
					drawBlock();
				}
			}
			if(keyCode==KeyEvent.VK_UP){//위 방향키
				int[] tempX=new int[4];
				int[] tempY=new int[4];
				
				for(int i=0;i<4;i++){
					tempX[i]=blockX[i];
					tempY[i]=blockY[i];
				}
				removeBlock();
				turnBlock();
				if(checkTurn()){
					turnAudio.play();
					if(blockPos<4){
						blockPos++;
					}
					else{
						blockPos=0;
					}
				}
				else{
					for(int i=0;i<4;i++){
						blockX[i]=tempX[i];
						blockY[i]=tempY[i];
						map[blockX[i]][blockY[i]]=true;
						colorMap[blockX[i]][blockY[i]]=colorType[blockType];
					}
				}
			}
			drawBlock();
			drawMap();
			drawGrid();
			repaint();
		}

		public void turnBlock() {
			// TODO Auto-generated method stub
			switch(blockType){
			case 1:
				switch(blockPos){
				case 0:
					blockX[0]=blockX[0];blockY[0]=blockY[0];
					blockX[1]=blockX[1];blockY[1]=blockY[1];
					blockX[2]=blockX[2];blockY[2]=blockY[2];
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
					break;
				case 1:
					blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-1;
					blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
					break;
				case 2:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1];blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2];blockY[2]=blockY[2]+1;
					blockX[3]=blockX[3];blockY[3]=blockY[3]+1;
					break;
				case 3:
					blockX[0]=blockX[0];blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1];
					blockX[2]=blockX[2]-1;blockY[2]=blockY[2];
					blockX[3]=blockX[3]+1;blockY[3]=blockY[3]-1;
					break;
				}break;
			case 2:
				switch(blockPos){
				case 0:
					blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2];blockY[2]=blockY[2];
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
					break;
				case 1:
					blockX[0]=blockX[0];blockY[0]=blockY[0];
					blockX[1]=blockX[1];blockY[1]=blockY[1];
					blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-1;
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]-1;
					break;
				case 2:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1];blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+2;
					blockX[3]=blockX[3]+2;blockY[3]=blockY[3]+1;
					break;
				case 3:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1];
					blockX[2]=blockX[2];blockY[2]=blockY[2]-1;
					blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
					break;
				}break;
			case 3:
				switch(blockPos){
				case 0:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1];
					blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+1;
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
					break;
				case 1:
					blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2]+1;blockY[2]=blockY[2]-2;
					blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
					break;
				case 2:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1];
					blockX[2]=blockX[2]-1;blockY[2]=blockY[2]+1;
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+1;
					break;
				case 3:
					blockX[0]=blockX[0];blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2]+1;blockY[2]=blockY[2];
					blockX[3]=blockX[3]+2;blockY[3]=blockY[3]-1;
					break;
				}break;
			case 4:
				switch(blockPos){
				case 0:
				case 2:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+2;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2]-1;blockY[2]=blockY[2];
					blockX[3]=blockX[3];blockY[3]=blockY[3]-1;
					break;
				case 1:
				case 3:
					blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]-2;blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2]+1;blockY[2]=blockY[2];
					blockX[3]=blockX[3];blockY[3]=blockY[3]+1;
					break;
				}break;
			case 5:
				switch(blockPos){
				case 0:
				case 2:
					blockX[0]=blockX[0]-1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2];blockY[2]=blockY[2];
					blockX[3]=blockX[3]+2;blockY[3]=blockY[3]-1;
					break;
				case 1:
				case 3:
					blockX[0]=blockX[0]+1;blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2];blockY[2]=blockY[2];
					blockX[3]=blockX[3]-2;blockY[3]=blockY[3]+1;
					break;
				}break;
			case 6:
				switch(blockPos){
				case 0:
				case 2:
					blockX[0]=blockX[0]+2;blockY[0]=blockY[0];
					blockX[1]=blockX[1]+1;blockY[1]=blockY[1]+1;
					blockX[2]=blockX[2];blockY[2]=blockY[2]+2;
					blockX[3]=blockX[3]-1;blockY[3]=blockY[3]+3;
					break;
				case 1:
				case 3:
					blockX[0]=blockX[0]-2;blockY[0]=blockY[0];
					blockX[1]=blockX[1]-1;blockY[1]=blockY[1]-1;
					blockX[2]=blockX[2];blockY[2]=blockY[2]-2;
					blockX[3]=blockX[3]+1;blockY[3]=blockY[3]-3;
					break;
				}
			}
		}
		public boolean checkTurn() {
			// TODO Auto-generated method stub
			boolean turnOK=true;
			for(int i=0;i<4;i++){
				if((blockX[i]>=0)&&(blockX[i]<12)&&(blockY[i]>=0)&&(blockY[i]<21)){
					if(map[blockX[i]][blockY[i]])turnOK=false;
				}
				else{
					turnOK=false;
				}
			}
			return turnOK;
		}

		public boolean checkMove(int dir) {
			// TODO Auto-generated method stub
			boolean moveOK=true;
			removeBlock();
			for(int i=0;i<4;i++){
				if(((blockX[i]+dir)>=0)&&((blockX[i]+dir)<12)){
					if(map[blockX[i]+dir][blockY[i]])moveOK=false;
				}
				else{
					moveOK=false;
				}
			}
			if(!moveOK) drawBlock();
			return moveOK;
		}
		
	}
}

