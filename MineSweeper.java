import java.io.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MineSweeper extends Application{

	static RestartButton restartButton;

	static CoverButton buttons[][];

	static int cellCount = 25;

	static int rowSize = 8;
	
	static int colSize = 8;
	
	static int bombCount = 0;
	
	static int flagsSet = 10;
	
	static int level = 1;
	
	static boolean gameOver = false;

	public static void main(String[] args) {

		launch(args);

	}
	
	public void start(Stage theStage){
		
		BorderPane bp = new BorderPane();

		HBox hbox = new HBox();

		GridPane sweep = new GridPane();

		MenuBar menuBar = new MenuBar();
		
		Menu difficulty = new Menu("Difficulty");
		
		menuBar.getMenus().add(difficulty);
		
		MenuItem beginner = new MenuItem("Beginner");
		
		beginner.setOnAction(click -> {
			
			level = 1;
			
			levelSetup(theStage);
			
		});
		
		MenuItem intermediate = new MenuItem("Intermediate");
		
		intermediate.setOnAction(click -> {			
			
			level = 2;
			
			levelSetup(theStage);	
			
		});
		
		MenuItem expert = new MenuItem("Expert");
		
		expert.setOnAction(click ->{
			
			level = 3;
			
			levelSetup(theStage);
			
		});
		
		difficulty.getItems().addAll(beginner, intermediate, expert);
		
		bp.setTop(menuBar);
		
		hbox.setStyle("-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:5; -fx-border-radius: 0.001;");
		
		bp.setStyle("-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:5; -fx-border-radius: 0.001;");
		
		bp.setBottom(sweep);

		hbox.setAlignment(Pos.CENTER);

		bp.setCenter(hbox);

		restartButton = new RestartButton();	
		
		restartButton.setOnAction(click ->{
			
			System.out.println("reset click");
			
			levelSetup(theStage);
		
			
			
		});		
		
		updateBanner(hbox);
		
		buttons = new CoverButton[rowSize][colSize];
		
		for(int y = 0; y < rowSize; y++) {

			for(int x = 0; x < colSize; x++) {

				buttons[y][x] = new CoverButton();

				CoverButton b = buttons[y][x]; 

				b.setGraphic(b.cover);
				
				b.setOnAction(click ->{
					
					System.out.println("left click");
					
					b.state = 1;

					if(!gameOver) {
						
						if(b.value == 0) {
							
							b.revealed = b.zeroImage;
							
							cellCount--;
							
						}else if(b.value == 1) {

							b.revealed = b.oneImage;

							cellCount--;

						}else if(b.value == 2) {

							b.revealed = b.twoImage;

							cellCount--;

						}else if(b.value == 3) {

							b.revealed = b.threeImage;

							cellCount--;

						}else if(b.value == 4) {

							b.revealed = b.theBombImage;

							MineSweeper.restartButton.setGraphic(RestartButton.deadFace);
							
							gameOver = true;

						}else if(b.value == 5) {	

							b.revealed = b.theFlag;
						
							cellCount--;
							
							bombCount--;
							
						}
					
						b.setGraphic(b.revealed);
						
					}

					checkWin();
					
				});

				b.setOnMouseClicked(click -> {
					
					MouseButton mouseButton = click.getButton();
					
					if(mouseButton == MouseButton.SECONDARY) {		
						
						checkWin();
						
						if(!gameOver) {
						
							if(b.isFlagged && flagsSet < 9) {		
							
								b.setGraphic(b.cover);
							
								b.value = b.originalValue;
								
								b.isFlagged = false;
								
								flagsSet++;
						
							}else if(!b.isFlagged && flagsSet > 0) {
							
								b.setGraphic(b.theFlag);						
							
								b.originalValue = b.value;
								
								b.value = 5;
								
								b.isFlagged = true;
								
								flagsSet--;
								
							}
						
						}
						
					}
						
					updateBanner(hbox);
							
				});
				
				sweep.add(buttons[y][x], y, x);

				/*int random = (int) (Math.random() * 100);
				
				if (random > 75) {
					
					buttons[y][x].value = 4;
					
				}*/
				
			}

		}

		placeBombs(buttons);
	
		
		
		for(int y = 0; y < rowSize; y++) {
			
			for(int x = 0; x < colSize; x++) {
				
				if(buttons[y][x].value != 4) {
					
					buttons[y][x].value = checkDanger(buttons, x, y);
					
				}
				
			}
			
		}
		
		theStage.setScene(new Scene(bp));

		theStage.show();
	
	}
	
	public void placeBombs(CoverButton[][] b) {
		
		for(int i = 0; i < flagsSet; i++) {
			
			tryBombPlacement(b);
			
		}
		
	}
	
	public void tryBombPlacement(CoverButton[][] b) {
		
		int x = (int) (Math.random() * rowSize - 1);
		
		int y = (int) (Math.random() * colSize - 1);
		
		System.out.println("y " + y + " x " + x);
		
		if(b[x][y].value == 4) {
			
			tryBombPlacement(b);
			
		} else {
			
			b[x][y].value = 4;
			
		}
		
	}
	
	public void levelSetup(Stage theStage) {
		
		System.out.println("level set up " + level);
		
		if(level == 1) {
			
			flagsSet = 10;
			
			rowSize = 8;
			
			colSize = 8;
			
			MineSweeper app = new MineSweeper();
			
			app.start(theStage);
		
			gameOver = false;			
	
		}else if(level == 2) {
		
			flagsSet = 40;
			
			rowSize = 16;
			
			colSize = 16;
			
			MineSweeper app = new MineSweeper();
		
			app.start(theStage);
		
			gameOver = false;
		
		}else if(level == 3) {
			
			flagsSet = 99;
			
			rowSize = 32;
			
			colSize = 16;
			
			MineSweeper app = new MineSweeper();
			
			app.start(theStage);
			
			gameOver = false;
			
		}else {
			
			System.err.println("level does not = 1, 2, 3 got " + level);
			
		}
		
	}
	
	public void updateBanner(HBox hbox) {
		
		int hundreth = 0; int  tenth = 0; int ones = 0;
		
		if(flagsSet >= 100) {
			
			hundreth = flagsSet / 100;
			
			tenth = (flagsSet % 100) / 10;
			
			ones = (flagsSet % 100) % 10;
			
		}else if(flagsSet >= 10 && flagsSet < 100) {
			
			tenth = flagsSet / 10;
			
			ones = (flagsSet % 10);
			
		}else {
			
			ones = flagsSet;
			
		}
		
		System.out.println("flagsSet " + flagsSet);
		
		hbox.getChildren().clear();
		
		ImageView minesLeftBox1 = new ImageView(new Image("file:res/digits/" + hundreth + ".png"));	

		ImageView minesLeftBox2 = new ImageView(new Image("file:res/digits/" + tenth + ".png"));
		
		ImageView minesLeftBox3 = new ImageView(new Image("file:res/digits/" + ones + ".png"));
		
		ImageView timerBox1 = new ImageView(new Image("file:res/digits/0.png"));
		
		ImageView timerBox2 = new ImageView(new Image("file:res/digits/0.png"));
		
		ImageView timerBox3 = new ImageView(new Image("file:res/digits/0.png"));
		
		hbox.getChildren().addAll(minesLeftBox1, minesLeftBox2, minesLeftBox3, restartButton, timerBox1, timerBox2, timerBox3);
		
	}

	public void checkWin() {	
		
		int opened = 0;

		for(int y = 0; y < rowSize; y++) {

			for(int x = 0; x < colSize; x++) {

				if (buttons[y][x].state == 1 && buttons[y][x].value != 4 ) {
					
					opened++;

				}

			}

		}

		if(opened == 22) {
			
			MineSweeper.restartButton.setGraphic(RestartButton.winFace);
			
			gameOver = true;
		
		}

	}

	public void checkLose(CoverButton[][] buttons) {
		
		if(gameOver) {
			
			for (int y = 0; y < rowSize; y++) {
				
				for (int x = 0; x < colSize; x++ ) {
					
					if(!buttons[y][x].isOpened) {
						
						
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public int checkDanger(CoverButton[][] grid, int x, int y) {
		
		int dangerCount = 0;
		
		if(x > 0){
			
			if(isDanger(grid, x - 1, y)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(y > 0) {
			
			if(isDanger(grid, x, y - 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(x > 0 && y > 0) {
			
			if(isDanger(grid, x - 1, y - 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(x != colSize - 1 && y > 0) {
			
			if(isDanger(grid, x + 1, y - 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(x != colSize - 1) {
			
			if(isDanger(grid, x + 1, y)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(y != rowSize - 1) {
			
			if(isDanger(grid, x, y + 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(x != colSize - 1 && y != rowSize - 1) {
			
			if(isDanger(grid, x + 1, y + 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		if(x > 0 && y != rowSize - 1) {
			
			if(isDanger(grid, x - 1, y + 1)) {
				
				dangerCount++;
				
			}
			
		}
		
		return dangerCount;
		
	}
	
	public boolean isDanger(CoverButton[][] grid, int x, int y) {
		
		if(grid[y][x].value == 4) {
			
			return true;
			
		}
		
		return false;
		
	}
	
}


class RestartButton extends Button{

	static ImageView smileyFace;

	static ImageView deadFace;

	static ImageView winFace;

	ImageView astonishedFace;

	public RestartButton() {

		double restartButtonSize = 45;

		setMinWidth(restartButtonSize);

		setMinHeight(restartButtonSize);

		setMaxWidth(restartButtonSize);

		setMaxHeight(restartButtonSize);

		smileyFace = new ImageView(new Image("file:res/face-smile.png"));

		smileyFace.setFitWidth(restartButtonSize);

		smileyFace.setFitHeight(restartButtonSize);

		deadFace = new ImageView(new Image("file:res/face-dead.png"));

		deadFace.setFitWidth(restartButtonSize);

		deadFace.setFitHeight(restartButtonSize);

		winFace = new ImageView(new Image("file:res/face-win.png"));

		winFace.setFitWidth(restartButtonSize);

		winFace.setFitHeight(restartButtonSize);

		astonishedFace = new ImageView(new Image("file:res/face-O.png"));

		astonishedFace.setFitWidth(restartButtonSize);

		astonishedFace.setFitHeight(restartButtonSize);

		setGraphic(smileyFace);

	}

}

class CoverButton extends Button{

	int state, value, originalValue;
	
	boolean isFlagged, isOpened;
	
	ImageView cover, revealed, zeroImage, oneImage, twoImage, threeImage, theBombImage, theFlag, hiddenBomb;

	public CoverButton() {

		isFlagged = false;
		
		isOpened = false;
		
		value = 0;
		
		originalValue = 0;

		state = 0;

		double size = 45;

		setMinWidth(size);

		setMinHeight(size);

		setMaxWidth(size);

		setMaxHeight(size);

		cover = new ImageView(new Image("file:res/cover.png"));

		cover.setFitWidth(size);

		cover.setFitHeight(size);

		zeroImage = new ImageView(new Image("file:res/0.png"));

		zeroImage.setFitWidth(size);

		zeroImage.setFitHeight(size);

		oneImage = new ImageView(new Image("file:res/1.png"));

		oneImage.setFitWidth(size);

		oneImage.setFitHeight(size);

		twoImage = new ImageView(new Image("file:res/2.png"));

		twoImage.setFitWidth(size);

		twoImage.setFitHeight(size);

		threeImage = new ImageView(new Image("file:res/3.png"));

		threeImage.setFitWidth(size);

		threeImage.setFitHeight(size);

		theBombImage = new ImageView(new Image("file:res/mine-red.png"));

		theBombImage.setFitWidth(size);

		theBombImage.setFitHeight(size);

		theFlag = new ImageView(new Image("file:res/flag.png"));
		
		theFlag.setFitWidth(size);
		
		theFlag.setFitHeight(size);
		
		hiddenBomb = new ImageView(new Image("file:res/bombrevealed-bicubic.png"));
		
		hiddenBomb.setFitWidth(size);
		
		hiddenBomb.setFitHeight(size);
		
		setGraphic(cover);

	}

}