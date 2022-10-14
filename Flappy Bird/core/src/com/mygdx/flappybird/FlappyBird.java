package com.mygdx.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backGround;
	Texture[] birds;
	int flapState=0;
	Texture gameOver;
	float birdY=0;
	int score=0;
	int scoringTube=0;
	BitmapFont font;
	Circle birdCircle;
	//ShapeRenderer shapeRenderer;
	float velocity=0;
	int gameState=0;
	float gravity=2;
	Texture topTube;
	Texture bottomTube;
	float gap=400;
	float maximumTubeOffset;
	Random random;
	float tubeVelocity=4;
	int noOfTubes=4;
	float[] tubeX= new float[noOfTubes];
	float distanceBetweenTubes;
	float[] tubeOffset=new float[noOfTubes];
	Rectangle[] topTubesRectangles;
	Rectangle[] bottomTubesRectangles;


	@Override
	public void create () {
		batch = new SpriteBatch();
		backGround=new Texture("bg.png");
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");
		topTube=new Texture("toptube.png");
		bottomTube=new Texture("bottomtube.png");
		gameOver=new Texture("gameover.png");

		maximumTubeOffset=Gdx.graphics.getHeight()/2-gap/2-100;
		random=new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		distanceBetweenTubes=Gdx.graphics.getWidth()*3/4;
		//shapeRenderer=new ShapeRenderer();
		birdCircle=new Circle();
		topTubesRectangles=new Rectangle[noOfTubes];
		bottomTubesRectangles=new Rectangle[noOfTubes];
		startGame();

	}

	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

		for(int i=0;i<noOfTubes;i++){
			tubeOffset[i]=(random.nextFloat()-	0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2+Gdx.graphics.getWidth()	+i*distanceBetweenTubes;

			topTubesRectangles[i]=new Rectangle();
			bottomTubesRectangles[i]=new Rectangle();

		}


	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(backGround, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState==1) {
			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score",String.valueOf(score));
				if(scoringTube<noOfTubes-1){
					scoringTube++;
				}else {
					scoringTube=0;
				}
			}
			if(Gdx.input.justTouched()){

				velocity=-20;

			}
			for(int i=0;i<noOfTubes;i++) {
				if(tubeX[i]<-topTube.getWidth()){
					tubeX[i]+=noOfTubes*distanceBetweenTubes;
					tubeOffset[i]=(random.nextFloat()-	0.5f)*(Gdx.graphics.getHeight()-gap-200);
				}else
				{
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubesRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubesRectangles[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
			}


			if(birdY>0) {
				velocity = velocity + gravity;
				birdY -= velocity;
			}else
			{
				gameState=2;
			}
		}else if(gameState==0)
		{

			if(Gdx.input.justTouched()){

				gameState=1;
			}
		}else if(gameState==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
			if(Gdx.input.justTouched()){

				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}
		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch,String.valueOf(score),100,200);


		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		 shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i=0;i<noOfTubes;i++){
//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle,topTubesRectangles[i]) || Intersector.overlaps(birdCircle,bottomTubesRectangles[i])){
				gameState=2;
			}
		}
		batch.end();
		//shapeRenderer.end();

	}

}
