package subt;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Subtitles {
	static String tytul="";
	
	static Long start=null;
	public static void wolniej5(){
		start=start+5000;
	}
	public static void wolniej01(){
		start=start+100;
	}
	public static void szybciej5(){
		start=start-5000;
	}
	public static void szybciej01(){
		start=start-100;
	}
	public static void ustawczas(String s){
		String tab[] = s.split(":");
		start=System.currentTimeMillis()-(Long.parseLong(tab[0])*3600000+Long.parseLong(tab[1])*60000+Long.parseLong(tab[2])*1000); 
	}
	
	public static void main(String[] arg) throws IOException{
		
		Zrodlo z = new Zrodlo();
		z.go();
		start =  System.currentTimeMillis();
		Thread napisy = new Napisy();
		Thread timer = new Timer(napisy);
		napisy.start();
		timer.start();
		
	}
	static class Zrodlo{
		boolean war=true;
		
		private void myexit(){
			war=false;
		}
		public void go(){
			int X=500,Y=150;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		JFrame f = new JFrame();
		f.setLocation((int)((width/2)-(X/2)), (int)((height/2)-(Y/2)));
		JTextField t = new JTextField("davinci.srt");
		JTextField t2 = new JTextField("Podaj plik z napisami znajduj¹cy siê na pulpicie wraz z rozszerzeniem");
		t2.setEditable(false);
		t.setSize(80,10);
		JButton b = new JButton("Start");
		f.setLayout(new GridLayout(3,1));
		f.setSize(X,Y);
		f.add(t2);
		f.add(t);
		f.add(b);
		f.setVisible(true);
		
		b.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				tytul=t.getText();
				f.setVisible(false);
				f.dispose();
				myexit();
				
			}
			
		});
		while(war){
			System.out.println("");
		}
		}
		
	}
	static class Napisy extends Thread{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		String path = System.getProperty("user.home")+"\\Desktop\\"+tytul;
	
		JFrame f = new JFrame();
		JPanel p = new JPanel();
		JTextArea t = new JTextArea();
		public BufferedReader br=null;
		public boolean isReset=false;
		public void run(){
		try{
		t.setEditable(false);
		t.setFont(new Font("Serif",Font.PLAIN,20));
		p.add(t);
		f.add(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(true);
		f.setSize((int)width/2, 100);
		f.setLocation((int)width/4, (int)height-138);
		f.setVisible(true);
		
	    long elapsed;
		String line;
		boolean war1=true,war2=true;
		br = new BufferedReader(new FileReader(new File(path)));
		String str=null;
		String czas=null;
		String text="";
		String[] tab=null;
		String[] tab2=null;
		String[] tab3=null;
		long milisbegin, milisend;
		while(true){
			System.out.println("");
			if(isReset==true){
				br.close();
				br = new BufferedReader(new FileReader(new File(path)));
				isReset=false;

			}
			milisbegin=milisend=0;
			str=br.readLine();
			czas=br.readLine();
			text="";
			while(!(line=br.readLine()).equals("")){
			line=removeHTML(line);	
			text+=line;
			text+="\n";
			}
			text=text.substring(0,text.length()-1);
			
			tab=czas.split(":");
		    tab2=tab[2].split(",");
		    tab3=tab2[1].split(" ");
			milisbegin+=3600000*Integer.parseInt(tab[0]);
			milisbegin+=60000*Integer.parseInt(tab[1]);
			milisbegin+=1000*Integer.parseInt(tab2[0]);
			milisbegin+=Integer.parseInt(tab3[0]);
			
			milisend+=3600000*Integer.parseInt(tab3[2]);
			milisend+=60000*Integer.parseInt(tab[3]);
			tab2=tab[4].split(",");
			milisend+=1000*Integer.parseInt(tab2[0]);
			milisend+=Integer.parseInt(tab2[1]);
			
			
			war1=war2=true;
			while(war1&&(isReset==false)){
				elapsed = System.currentTimeMillis()-start;
			System.out.println("");
				if(milisbegin<elapsed){
					t.setText(text);
					System.out.println("");
					while(war2&&(isReset==false)){
				System.out.println("");
						elapsed = System.currentTimeMillis()-start;
						if(milisend<elapsed){
							war1=war2=false;
							t.setText("");
						}
					}
				}
				System.out.println("");
			}
		}
		}catch(Exception e){
			
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		public String removeHTML(String line){
			if(line.matches(".*[<][^>]*[>].*")){
				line=line.substring(0,line.indexOf("<"))+line.substring(line.indexOf(">")+1,line.length());
				line=removeHTML(line);
			}
			line.trim();
			return line;
		}
	}
	static class Timer extends Thread{
		Napisy napisy=null;
		public Timer(Thread n){
			napisy=(Napisy)n;
		}
		JFrame fr = new JFrame();
		JPanel pa = new JPanel();
		JTextField fi = new JTextField("00:00:00");
		JTextField fi2 = new JTextField();
		
		JButton b = new JButton("-5 sek");
		JButton b2 = new JButton("+5 sek");
		JButton b3 = new JButton("-0.1 sek");
		JButton b4 = new JButton("+0.1 sek");
		JButton b5 = new JButton("Przeskocz");
		
		public void run(){
			fr.setLayout(new GridLayout(7,1));
			fr.setSize(80, 260);
			fr.setVisible(true);
			fi.setEditable(false);
			b.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					wolniej5();
					napisy.isReset=true;
				}
				
			});
	b2.addActionListener(new ActionListener(){
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					szybciej5();
				}
				
			});
	b3.addActionListener(new ActionListener(){
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			wolniej01();
		}
		
	});
	b4.addActionListener(new ActionListener(){
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			szybciej01();
		}
		
	});
	b5.addActionListener(new ActionListener(){
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ustawczas(fi2.getText());
			napisy.isReset=true;
		}
		
	});
			
			pa.add(fi);
			fr.add(pa);
			fr.add(b);
			fr.add(b2);
			fr.add(b3);
			fr.add(b4);
			fi2.setText("00:00:00");
			fr.add(fi2);
			fr.add(b5);
			
			
			fr.setVisible(true);
			//fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			long cur;
			long godz,min,sek;
			String s;
			while(true){
				
				cur=System.currentTimeMillis()-start;
				godz=cur/3600000;
				cur=cur-(3600000*godz);
				min = cur/60000;
				cur=cur-60000*min;
				sek=cur/1000;
				s="0"+Long.toString(godz)+":"+(min<10 ? "0" : "")+Long.toString(min)+":"+(sek<10 ? "0" : "") + Long.toString(sek);
				fi.setText(s);
			}
		}
	};
	
}


