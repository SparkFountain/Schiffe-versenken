AppTitle("Schiffe versenken")

Graphics(1024,768,32,2)
SetBuffer(BackBuffer())
SeedRnd(MilliSecs())

;die Schiffe "Gut/Schlecht";"groß/klein";"von links/von rechts"
Global gsl = LoadImage("Good_small_left.png")
MaskImage(gsl,255,0,255)
Global gsr = LoadImage("Good_small_right.png")
MaskImage(gsr,255,0,255)
Global gbl = LoadImage("Good_big_left.png")
MaskImage(gbl,255,0,255)
Global gbr = LoadImage("Good_big_right.png")
MaskImage(gbr,255,0,255)
Global bsl = LoadImage("Bad_small_left.png")
MaskImage(bsl,255,0,255)
Global bsr = LoadImage("Bad_small_right.png")
MaskImage(bsr,255,0,255)
Global bbl = LoadImage("Bad_big_left.png")
MaskImage(bbl,255,0,255)
Global bbr = LoadImage("Bad_big_right.png")
MaskImage(bbr,255,0,255)

;die Kanone und die Kugel
Global can = LoadImage("Kanone.png")
MaskImage(can,255,0,255)
Global bullet = LoadImage("Kugel.png")
MaskImage(bullet,255,0,255)

;die Level-Hintergruende
Global lvl1 = LoadImage("Level_1.png")
Global lvl2 = LoadImage("Level_2.png")
Global lvl3 = LoadImage("Level_3.png")
Global lvl4 = LoadImage("Level_4.png")
Global lvl5 = LoadImage("Level_5.png")
Global lvl6 = LoadImage("Level_6.png")

;die Buttons und der Mauszeiger
Global b_Start = LoadImage("Button_Start.png")
Global b_Level = LoadImage("Button_Level.png")
Global b_Credits = LoadImage("Button_Credits.png")
Global b_End = LoadImage("Button_End.png")
Global yes = LoadImage("Yes.png")
Global no = LoadImage("No.png")

Global pointer = LoadImage("Mauszeiger.png")
MaskImage(pointer,255,0,255)

;die Sounds
Global shot = LoadSound("Shot.wav")
Global ch1
Global exp1 = LoadSound("explosion.wav")
Global ch2
Global exp2 = LoadSound("explosion2.wav")
Global ch3
Global exp3 = LoadSound("explosion3.wav")
Global ch4
Global exp_wh = LoadSound("explosion_white.wav")
Global ch5
Global backChn
Global back = LoadSound("Back.ogg")
Global lvlChn
Global level1 = LoadSound("Level1.ogg")
Global level2 = LoadSound("Level2.ogg")
Global level3 = LoadSound("Level3.ogg")
Global level4 = LoadSound("Level4.ogg")
Global level5 = LoadSound("Level5.ogg")
Global level6 = LoadSound("Level6.ogg")


;andere Variablen
Global menu_select = 1
Global ingame = 1
Global level_select
Global show_credits
Global quit_game
Global play_game
Global level = 1

;im Spiel
Global timer
Global ship_kind
Global temp
Global score
Global countdown = 130

Global spoint
Global bpoint
Global bullet_x = 545

Global min
Global sec

Global set
Global t

Global min_score = 330
Global wait = 0
Global y_move#

Global all_score
Global file
Global check 

;Koordinaten
Global set_ship_left = -200
Global set_ship_right = 1024
Global m_left_1# = (2.7)
Global m_left_2# = (3.4)
Global m_left_3# = (4.2)
Global m_left_4# = (4.8)
Global m_left_5# = (5.5)
Global m_left_6# = (6.1)
Global rnd_b = 400
Global rnd_s1 = 175
Global rnd_s2 = 375
Global rnd_s3 = 350
Global rnd_s4 = 350
Global rnd_s5 = 225
Global rnd_s6 = 325
Global ship_speed
Global shoot_allow = 1

Const l1 = 1500
Const l2 = 1300
Const l3 = 1100
Const l4 = 900
Const l5 = 700
Const l6 = 500

;die Schrift
Global font = LoadFont("Hollywood Hills",24,1,0,0)
SetFont(font)
Color(120,200,50)

;Hauptschleife
While(ingame=1)
	Cls()
	
	DrawMenu()
	CheckButtons()
	SelectLevel()
	ShowCredits()
	Quit()
	DrawLevel()
	DrawShips()
	Shoot()
	CalculateScore()
	
	Flip(0)
Wend
End()


;zeichnet das Hauptmenü
Function DrawMenu()
	If(menu_select=1) Then
		TileBlock(lvl4)
		DrawBlock(b_Start,100,600)
		DrawBlock(b_Level,300,600)
		DrawBlock(b_Credits,610,600)
		DrawBlock(b_End,810,600)
		DrawImage(pointer,MouseX(),MouseY())
		Text(400,0,"Schiffe versenken 1.0")
		If(ChannelPlaying(backChn) = 0) Then
			LoopSound(back)
			backChn = PlaySound(back)
		EndIf
	EndIf
End Function

;überprüft, ob Taste gedrückt wurde
Function CheckButtons()
	If(menu_select) Then
		If(ImagesOverlap(pointer,MouseX(),MouseY(),b_Start,100,600) And MouseDown(1)) Then
			menu_select = 0
			play_game = 1
		EndIf
		If(ImagesOverlap(pointer,MouseX(),MouseY(),b_Level,300,600) And MouseDown(1)) Then
			menu_select = 0
			level_select = 1
		EndIf
		If(ImagesOverlap(pointer,MouseX(),MouseY(),b_Credits,650,600) And MouseDown(1)) Then
			menu_select = 0
			show_credits = 1
		EndIf
		If(ImagesOverlap(pointer,MouseX(),MouseY(),b_End,850,600) And MouseDown(1)) Then
			menu_select = 0
			quit_game = 1
		EndIf
	EndIf
End Function


;Level auswählen
Function SelectLevel()
	If(level_select) Then
		TileBlock(lvl2)
		Text(100,450,"[x]: nächster Level")
		Text(100,500,"[y]: vorheriger Level")
		Text(100,550,"Zurück ins Hauptmenü mit [Esc]")
		Text(450,450,"Gewählter Level: "+level)
		If(KeyHit(45) And level<6) Then
			level = level+1
			min_score = (min_score*2)
		ElseIf(KeyHit(44) And level>1) Then
			level = level-1
			min_score = (min_score/2)
		EndIf 
		If(KeyHit(1)) Then
			level_select = 0
			menu_select = 1
		EndIf	
	EndIf
End Function

;die Credits anzeigen
Function ShowCredits()
	If(show_credits) Then
		TileBlock(lvl6)
		Text(512,600-y_move#,"Dein Highscore: "+all_score+" Punkte!",1,1)
		Text(512,900-y_move#,"Schiffe versenken 1.0",1,1)
		Text(512,960+60-y_move#,"Idee: Die Idee für das Spiel stammt aus der",1,1)
		Text(512,990+60-y_move#,"Projektwoche von 2004 am Ökumenischen Domgymnasium Magdeburg",1,1)
		Text(512,1050+60-y_move#,"Programmierung, Sounds und Musik: Stefan Behrendt",1,1)
		Text(512,1110+60-y_move#,"Grafik: Eldon Matthia",1,1)
		Text(512,1160+60-y_move#,"Leveldesign: Christian Radius, Eldon Matthia",1,1)
		Text(512,1250+60-y_move#,"Damals wollten wir in vier oder fünf Tagen ein Computerspiel",1,1)
		Text(512,1280+60-y_move#,"programmieren, völlig ohne Vorkenntnisse. Die einzige an",1,1)
		Text(512,1310+60-y_move#,"unserer SChule verfügbare Programmiersprache, Turbo Pascal,",1,1)
		Text(512,1340+60-y_move#,"half uns nicht weiter; letztendlich scheiterte unser Projekt",1,1)
		Text(512,1370+60-y_move#,"kläglich als animierte PowerPoint-Präsentation :D.",1,1)
		Text(512,1400+60-y_move#,"Als ich diese alten Dateien vor Kurzem beim Durchblättern",1,1)
		Text(512,1430+60-y_move#,"entdeckte, hatte ich die Idee, dieses 'Spiel' - wenn auch",1,1)
		Text(512,1460+60-y_move#,"mit 5 Jahren Verspätung, doch noch zu vollenden.",1,1)
		
		y_move# = y_move#+0.5
		
		If(KeyHit(1)) Then
			show_credits = 0
			menu_select = 1
			y_move = 0
			StopChannel(lvlChn)
		EndIf
	EndIf
End Function 


;fragen ob beendet werden soll
Function Quit()
	If(quit_game) Then
		TileBlock(lvl1)
		Text(512,250,"Bist du sicher, dass du das Spiel beenden willst?",1,1)
		DrawBlock(yes,350,300)
		DrawBlock(no,550,300)
		DrawImage(pointer,MouseX(),MouseY())
		
		If(ImagesOverlap(pointer,MouseX(),MouseY(),yes,350,300) And MouseDown(1)) Then ingame = 0
		If(ImagesOverlap(pointer,MouseX(),MouseY(),no,550,300) And MouseDown(1)) Then
			quit_game = 0
			menu_select = 1
		EndIf
	EndIf
End Function 


;Level zeichnen und Sound erstellen
Function DrawLevel()

	If(play_game And wait=0) Then
		StopChannel(backChn)
		Select(level)
			Case 1
				TileBlock(lvl1)
				DrawImage(can,0,550)
				ship_speed = l1
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level1
				lvlChn = PlaySound(level1)
			EndIf
			Case 2
				TileBlock(lvl2)
				DrawImage(can,0,550)
				ship_speed = l2
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level2
				lvlChn = PlaySound(level5)
			EndIf
			Case 3
				TileBlock(lvl3)
				DrawImage(can,0,550)
				ship_speed = l3
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level3
				lvlChn = PlaySound(level2)
			EndIf
			Case 4
				TileBlock(lvl4)
				DrawImage(can,0,550)
				ship_speed = l4
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level4
				lvlChn = PlaySound(level6)
				EndIf
			Case 5
				TileBlock(lvl5)
				DrawImage(can,0,550)
				ship_speed = l5
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level5
				lvlChn = PlaySound(level3)
				EndIf
			Case 6
				TileBlock(lvl6)
				DrawImage(can,0,550)
				ship_speed = l6
				If(ChannelPlaying(lvlChn)=0) Then
				LoopSound level6
				lvlChn = PlaySound(level4)
			EndIf
		End Select
		If(KeyHit(1)) Then
			StopChannel lvlChn
			play_game = 0
			menu_select = 1
		EndIf
	EndIf
End Function



;die Schiffe
Function DrawShips()
	If(play_game = 1 And wait=0) Then
		If(temp=0) Then
				timer = MilliSecs()
				ship_kind = Rnd(1,8)
				temp = 1
			EndIf
			
			If((MilliSecs()-ship_speed) > timer) Then
				temp = 0
			EndIf
				
			Local new_ship.Ship
			If(ship_kind=1 And temp=0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 0
				new_ship\size = 0
				new_ship\kind = 1
				new_ship\sort = 1
				new_ship\x = set_ship_left
				Select level
				Case 1
				new_ship\y = Rand(rnd_s1,300)
				Case 2
				new_ship\y = Rand(rnd_s2,300)
				Case 3
				new_ship\y = Rand(rnd_s3,300)
				Case 4
				new_ship\y = Rand(rnd_s4,300)
				Case 5
				new_ship\y = Rand(rnd_s5,300)
				Case 6
				new_ship\y = Rand(rnd_s6,300)
				End Select
			ElseIf(ship_kind = 2 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 1
				new_ship\size = 0
				new_ship\kind = 1
				new_ship\sort = 101
				new_ship\x = set_ship_right
				Select level
				Case 1
				new_ship\y = Rand(rnd_s1,300)
				Case 2
				new_ship\y = Rand(rnd_s2,300)
				Case 3
				new_ship\y = Rand(rnd_s3,300)
				Case 4
				new_ship\y = Rand(rnd_s4,300)
				Case 5
				new_ship\y = Rand(rnd_s5,300)
				Case 6
				new_ship\y = Rand(rnd_s6,300)
				End Select
			ElseIf(ship_kind = 3 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 0
				new_ship\size = 1
				new_ship\kind = 1
				new_ship\sort = 11
				new_ship\x = set_ship_left
				new_ship\y = Rand(300,rnd_b)
			ElseIf(ship_kind = 4 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 1
				new_ship\size = 1
				new_ship\kind = 1
				new_ship\sort = 111
				new_ship\x = set_ship_right
				new_ship\y = Rand(300,rnd_b)
			ElseIf(ship_kind = 5 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 0
				new_ship\size = 0
				new_ship\kind = 0
				new_ship\sort = 0
				new_ship\x = set_ship_left
				Select level
				Case 1
				new_ship\y = Rand(rnd_s1,300)
				Case 2
				new_ship\y = Rand(rnd_s2,300)
				Case 3
				new_ship\y = Rand(rnd_s3,300)
				Case 4
				new_ship\y = Rand(rnd_s4,300)
				Case 5
				new_ship\y = Rand(rnd_s5,300)
				Case 6
				new_ship\y = Rand(rnd_s6,300)
				End Select
			ElseIf(ship_kind = 6 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 1
				new_ship\size = 0
				new_ship\kind = 0
				new_ship\sort = 100
				new_ship\x = set_ship_right
				Select level
				Case 1
				new_ship\y = Rand(rnd_s1,300)
				Case 2
				new_ship\y = Rand(rnd_s2,300)
				Case 3
				new_ship\y = Rand(rnd_s3,300)
				Case 4
				new_ship\y = Rand(rnd_s4,300)
				Case 5
				new_ship\y = Rand(rnd_s5,300)
				Case 6
				new_ship\y = Rand(rnd_s6,300)
				End Select 
			ElseIf(ship_kind = 7 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 0
				new_ship\size = 1
				new_ship\kind = 0
				new_ship\sort = 10
				new_ship\x = set_ship_left
				new_ship\y = Rand(300,rnd_b)
			ElseIf(ship_kind = 8 And temp = 0) Then
				new_ship.Ship = New Ship
				new_ship\direction = 1
				new_ship\size = 1
				new_ship\kind = 0
				new_ship\sort = 110
				new_ship\x = set_ship_right
				new_ship\y = Rand(300,rnd_b)
			EndIf
			
			For new_ship.Ship = Each Ship
				If(new_ship\direction = 0) Then
					Select level
					Case 1
						new_ship\x = new_ship\x+m_left_1
					Case 2
						new_ship\x = new_ship\x+m_left_2
					Case 3
						new_ship\x = new_ship\x+m_left_3
					Case 4
						new_ship\x = new_ship\x+m_left_4
					Case 5
						new_ship\x = new_ship\x+m_left_5
					Case 6
						new_ship\x = new_ship\x+m_left_6
					End Select
				ElseIf(new_ship\direction = 1) Then
					Select level
					Case 1
					new_ship\x = new_ship\x - m_left_1
					Case 2
					new_ship\x = new_ship\x - m_left_2
					Case 3
					new_ship\x = new_ship\x - m_left_3
					Case 4
					new_ship\x = new_ship\x - m_left_4
					Case 5
					new_ship\x = new_ship\x - m_left_5
					Case 6
					new_ship\x = new_ship\x - m_left_6
					End Select
				EndIf
			Next
				
				
			For new_ship.Ship = Each Ship
				If(new_ship\direction = 0) Then
					If(new_ship\size = 0) Then
						If(new_ship\kind = 0) Then
							DrawImage(bsl,new_ship\x,new_ship\y)
						ElseIf(new_ship\kind = 1) Then
							DrawImage(gsl,new_ship\x,new_ship\y)
						EndIf
					ElseIf(new_ship\size = 1) Then
						If(new_ship\kind = 0) Then
							DrawImage(bbl,new_ship\x,new_ship\y)
						ElseIf(new_ship\kind = 1) Then
							DrawImage(gbl,new_ship\x,new_ship\y)
						EndIf
					EndIf
				ElseIf(new_ship\direction = 1) Then
					If(new_ship\size = 0) Then
						If(new_ship\kind = 0) Then
							DrawImage(bsr,new_ship\x,new_ship\y)
						ElseIf(new_ship\kind=1) Then
							DrawImage(gsr,new_ship\x,new_ship\y)
						EndIf
					ElseIf(new_ship\size = 1) Then
						If(new_ship\kind = 0) Then
							DrawImage(bbr,new_ship\x,new_ship\y)
						ElseIf(new_ship\kind = 1) Then 
							DrawImage(gbr,new_ship\x,new_ship\y)
						EndIf
					EndIf
				EndIf
			Next
		EndIf
End Function 

;Score, Levelanzeige und Countdown
Function CalculateScore()
	If(play_game = 1) Then
		Text(0,0,"Punkte: "+score)
		Text(200,0,"benötigte Punkte: "+min_score)
		Text(580,0,"Level: "+level)
		Text(0,635,"Highscore: "+all_score)
		If(sec>=10) Then
			Text(760,0,"Countdown: "+min+":"+sec)
		ElseIf(sec<10) Then
			Text(760,0,"Countdown: "+min+":0"+sec)
		EndIf
	
		Select(level)
			Case 1
				bpoint = 5
				spoint = 10
			Case 2
				bpoint = 10
				spoint = 20
			Case 3
				bpoint = 20
				spoint = 40
			Case 4
				bpoint = 30
				spoint = 60
			Case 5
				bpoint = 50
				spoint = 100
			Case 6
				bpoint = 75
				spoint = 150
		End Select
		
		If(score<0) Then score = 0
		
		If(set=0) Then
			t = MilliSecs()
			set = 1
		EndIf
		
		If((t+1000) < MilliSecs() And countdown >0) Then
			countdown = countdown-1
			set = 0
		EndIf
		
		min = (countdown - (countdown Mod 60))/60
		sec = countdown Mod 60
		
		If(countdown=0 And score<min_score) Then
			wait = 1
			TileBlock(lvl5)
			Text(250,200,"Du hast leider nicht genug Punkte erreicht!")
			Text(250,250,"Level wiederholen?")
			DrawBlock(yes, 150,500)
			DrawBlock(no,300,500)
			DrawImage(pointer,MouseX(),MouseY())
			If(ImagesOverlap(pointer,MouseX(),MouseY(),yes,150,500) And MouseDown(1)) Then
				play_game = 1
				countdown = 130
				all_score = all_score+score
				score = 0
				wait = 0
			ElseIf(ImagesOverlap(pointer,MouseX(),MouseY(),no,300,500) And MouseDown(1)) Then
				play_game = 0
				menu_select = 1
				countdown = 130
				score = 0
				wait = 0
				EndIf
			ElseIf(countdown=0 And score>=min_score) Then
				Delay 900
				StopChannel lvlChn
				If(level < 6) Then
				level = level+1
				countdown = 130
				all_score = all_score+score
				score = 0
				min_score = (min_score*2)
				wait = 0
			ElseIf(level = 6) Then
				lvlChn = PlaySound(back)
				play_game = 0
				show_credits = 1
				countdown = 130
				score = 0
				min_score = 330
				wait = 0
			EndIf
		EndIf			
	EndIf
	
End Function 	



;die Kanonenschüsse
Function Shoot()
	If(play_game=1 And wait=0) Then
		If(KeyHit(57) And shoot_allow=1) Then
			Local bull.Shoot = New Shoot
			ch1 = PlaySound(shot)
			bull\y = 540
			shoot_allow = 0
		EndIf
		
		For bull.Shoot = Each Shoot
			DrawImage(bullet,bullet_x,bull\y)
		Next
		
		For bull.Shoot = Each Shoot
		bull\y = bull\y - 4
		If(bull\y<237) Then
		Delete bull.Shoot
		shoot_allow = 1
	EndIf
		Next 
		
		;left = 0, small = 0, bad = 0
		Local new_ship.Ship
		For new_ship.Ship = Each Ship
			For bull.Shoot = Each Shoot
				If(ImagesOverlap(bullet,547,bull\y,bsl,new_ship\x,new_ship\y) And new_ship\sort=0) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score+spoint
					shoot_allow = 1
					ch2 = PlaySound(exp1)
				ElseIf(ImagesOverlap(bullet,547,bull\y,bsr,new_ship\x,new_ship\y) And new_ship\sort=100) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score+spoint
					shoot_allow = 1
					ch3 = PlaySound(exp2)
				ElseIf(ImagesOverlap(bullet,547,bull\y,bbl,new_ship\x,new_ship\y) And new_ship\sort=10) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score+bpoint
					shoot_allow = 1
					ch4 = PlaySound(exp3)
				ElseIf(ImagesOverlap(bullet,547,bull\y,bbr,new_ship\x,new_ship\y) And new_ship\sort=110) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score+bpoint
					shoot_allow = 1
					ch3 = PlaySound(exp3)
				ElseIf(ImagesOverlap(bullet,547,bull\y,gsl,new_ship\x,new_ship\y) And new_ship\sort=1) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score - spoint
					shoot_allow = 1
					ch5 = PlaySound(exp_wh)
				ElseIf(ImagesOverlap(bullet,547,bull\y,gsr,new_ship\x,new_ship\y) And new_ship\sort=101) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score - spoint
					shoot_allow = 1
					ch5 = PlaySound(exp_wh)
				ElseIf(ImagesOverlap(bullet,547,bull\y,gbl,new_ship\x,new_ship\y) And new_ship\sort=11) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score - bpoint
					shoot_allow = 1
					ch5 = PlaySound(exp_wh)
				ElseIf(ImagesOverlap(bullet,547,bull\y,gbr,new_ship\x,new_ship\y) And new_ship\sort=111) Then
					Delete new_ship.Ship
					Delete bull.Shoot
					score = score-bpoint
					shoot_allow = 1
					ch5 = PlaySound(exp_wh)
				EndIf
			Next 
		Next
	EndIf
End Function 

Type Ship
	Field direction	;0 = left, 1 = right
	Field size ; 0 = small, 1 = big
	Field kind ; 0 = bad, 1 = good
	Field sort ; Zusammensetzung aus den drei oberen Zahlen
	Field x
	Field y
End Type 

Type Shoot
	Field y
End Type
;~IDEal Editor Parameters:
;~C#Blitz3D