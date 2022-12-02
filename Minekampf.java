import java.util.Scanner;
import java.util.Random;

public class Minekampf{
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	
	public static final int ROW = 10;
	public static final int COL = 20;
	public static final int BOMB = 40;
	public static int [][] camp = new int [COL][ROW];
	//000:data 00000:number
	//visible, flag, bomb, numbers...
	public static int gameMode = 1;
	public static int[] pointer = new int[3];
	public static Scanner input = new Scanner(System.in);
	
	
	public static void main (String[] args){
		printCamp(gameMode);
		getPointer();
		generateCamp(pointer[0], pointer[1]);
		printCamp(gameMode);
		do {
			getPointer();
			checkBox(pointer[0], pointer[1], pointer[2]);
			printCamp(gameMode);
		}
		while(gameMode == 1);
		if (gameMode == 0){
			System.out.println("Hai perso!");
		}
		else{
			System.out.println("Hai vinto!");
		}
	}
	
	
	public static void generateCamp(int x, int y){
	Random rand = new Random();
	int pos;
		for(int i = 0; i < BOMB; i++){
			do{
				pos = rand.nextInt(ROW * COL);
			}
			while((camp[pos % COL][pos / COL] >> 5 == 1) || !((Math.abs((pos % COL) - x) > 2) || (Math.abs((pos / COL) - y) > 2)));
			camp[pos % COL][pos / COL] = 1;
			camp[pos % COL][pos / COL] <<= 5;
			neightbourOperator(pos % COL, pos / COL, 0);
		}
		checkBox(x, y, 0);
	}
	
	public static int neightbourOperator(int x, int y, int mode){
		int startX = (x - 1 >= 0 ? x - 1 : x);
		int endX = (x + 1 < COL ? x + 1 : x);
		int b = endX - startX + 1;
		int startY = (y - 1 >= 0 ? y - 1 : y);
		int endY = (y + 1 < ROW ? y + 1 : y);
		int h = endY - startY + 1;
		int area = b * h;
				
		switch (mode){
			case 0:{
				for (int a = 0; a < area; a++){
					if (camp[startX + (a % b)][startY + (a / b)] >> 5 != 1){
						camp[startX + (a % b)][startY + (a / b)]++;
					}
				}	
				return 0;
			}
		
			case 1:{
				camp[x][y] |= 1 << 7;
				for (int a = 0; a < area; a++){
					if (camp[startX + (a % b)][startY + (a / b)] >> 7 == 0){
						camp[startX + (a % b)][startY + (a / b)] |= 1 << 7;
						if (camp[startX + (a % b)][startY + (a / b)] % 16 == 0){
							neightbourOperator(startX + (a % b), startY + (a / b), 1);
						}
					}
				}	
					
				return 0;
			}
		}
		return 0;
	}
	
	public static void printCamp(int print_bomb){
		System.out.print("\033\143");
		for (int i = 0; i <= COL; i++){
			System.out.print("____");
		}
		System.out.println("");
		for(int r = 0; r < ROW; r++){
			System.out.print("|");
			for(int c = 0; c < COL; c++){
				if (camp[c][r] >> 7 == 1){
					if (camp[c][r] % 16 != 0){
						System.out.print(ANSI_GREEN + " " + Integer.toString(camp[c][r] % 16) + "  ");
					}
					else{
						System.out.print("    ");
					}
				}
				else if (print_bomb > 0 && (camp[c][r] >> 6) % 2 == 1){
						System.out.print(ANSI_BLUE + " ?  ");
					}
				else if (print_bomb == 0 && (camp[c][r] >> 5) % 2 == 1){
					System.out.print(ANSI_RED + " X  ");
				}
				else{
					//System.out.print(ANSI_GREEN + Integer.toString(camp[c][r] % 16) + "  ");
					System.out.print(ANSI_WHITE + (char)(r + 65) + (c) + " ");
					if (c < 10){
						System.out.print(ANSI_WHITE + " ");
					}
				}
			}
			System.out.print(ANSI_WHITE);
			System.out.print("|\n");
		}
		System.out.print((char)7);
		System.out.print((char)7);
		for (int i = 0; i <= COL; i++){
			System.out.print("____");
		}
		System.out.println("");
	}
	
	public static void getPointer(){
		String command = input.nextLine().toLowerCase().trim();
		int length = command.length();
		if (command.endsWith("f")){
			length--;
			pointer[2] = 1;
		}
		else {
			pointer[2] = 0;
		}
		pointer[1] = (int)command.charAt(0) - 97;
		pointer[0] = (int)command.charAt(1) - 48;
		if (length > 2){
			pointer[0] *= 10;
			pointer[0] += (int)command.charAt(2) - 48;
		}
	}
	
	public static void checkBox(int x, int y, int flag){
		if (flag == 1){
			camp[x][y] ^= 1 << 6;
		}
		else {
			camp[x][y] |= 1 << 7;
			if ((camp[x][y] >> 5) % 2 == 1){
				gameMode = 0;
			}
			else if (camp[x][y] % 16 == 0){
				neightbourOperator(x, y, 1);
			}
		}
	}
	
}
