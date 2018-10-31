package Game;

class Enemy
{
	int x;
	int y;
	boolean frontBehind;
	int walkNum;
	int deathNum;
	boolean live;

	Enemy(int x, int y, boolean frontBehind){
		this.x = x;
		this.y = y;
		this.walkNum = 0;
		this.frontBehind = frontBehind;
		this.deathNum = 0;
		this.live = true;
	}

	void move(int x, int y)
	{
		this.x += x;
		this.y += y;
	}

	void walking()
	{
		walkNum++;

		if(walkNum>10)
			walkNum=0;
	}

	void death()
	{
		deathNum++;
	}
}