package Game;


class Bullet
{
	int x;
	int y;
	int cntBullet;
	int damage;
	int type; // 1 : 용보스 기본  2 : 용보스 필살
	int moveSource;

	Bullet(int x, int y, int damage, int type)
	{
		this.x = x;
		this.y = y;
		this.damage = damage;
		cntBullet = 0;
		this.type = type;
		moveSource = 0;
	}

	Bullet(int x, int y, int damage, int type, int moveSource)
	{
		this.x = x;
		this.y = y;
		this.damage = damage;
		cntBullet = 0;
		this.type = type;
		this.moveSource = moveSource;	

	}

	void move(double x, double y)
	{
		this.x += x;
		this.y += y;
	}

	void bulletUse(int n)
	{
		cntBullet += n;
	}


}
