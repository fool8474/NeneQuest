package Game;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.sound.midi.Soundbank;
import javax.sound.sampled.*;

/**
 * 2017 - 08 - 03 
 * NEW GAME 1,3화의 NENE QUEST를 모델로 삼아 만들었습니다.
 * 만든이 : Anyang
 * 아이디어 : 사쿠라 네네
 * 그림 일부 : 사쿠라 네네
 * 이 외 제작 : Anyang
 * 홈페이지 : fool8474.blog.me
 * 코드를 마음껏 변형하시면 제 실력이 부족하여 버그가 마구 발생할 수 있으니 주의해주세요.
 * 코드를 열심히 분석하셔서 공부에 도움이 되었으면 좋겠지만 매우 대충 만들었기 때문에 코드의 질이 안 좋습니다. 참고해주세요.
 * 참고는 자유이며 공부하는데 쓰시면 좋겠지만, 본 작품을 수정하여 자신의 것이라고 주장하는 행위는 배제합니다.
 * @author Anyang
 * 
 * 
 * 2017 - 08 - 04
 * Ver1.0
 * 첫 배포!
 * 
 * 2017 - 08 - 05
 * Ver1.1
 * 패치노트
 * 1. 어느순간부터 무한점프를 하는 문제가 수정
 * 2. 게임 중간 ESC를 누르면 나가기가 적용
 * 3. 게임 중간 P를 누르면 멈추는것이 가능
 * 4. 시작시 게임 키 설명
 * 
 * 2017 - 08 - 07
 * Ver1.2
 * 패치노트
 * 1. 보스 출현할때 PAUSE를 누르면 배경이 움직이는 버그 수정
 * 2. 여러가지 텍스트를 영문으로 변경
 * 3. 적을 잡을때 사운드 추가
 * 4. 보스 피격시 효과 및 사운드 추가
 */


public class Main {
	public static void main(String[] args) {
		try
		{
			new mainFrame();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
