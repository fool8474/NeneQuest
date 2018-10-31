package Game;

class Boss
{
	int x;
	int y;
	double hp;
	int bossCnt;
	int bossState; //1 평상 2 공격 3 특수공격
	int attackCount;
	double initialHp;
	int deathNum;
	boolean live;

	Boss(int x,int y,double hp)
	{
		this.x = x;
		this.y = y;
		this.bossCnt = 0;
		this.hp = hp;
		this.bossState = 1;
		this.attackCount = 0;
		initialHp = hp;
		this.live = true;
	}

	void move(int x, int y)
	{
		this.x += x;
		this.y += y;
	}

	void bossActive()
	{
		bossCnt++;
	}

	void death()
	{
		deathNum++;
	}
}
