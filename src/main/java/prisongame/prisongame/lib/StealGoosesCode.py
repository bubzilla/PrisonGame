i = ["\\. ","~ ", ", ","~ ","- ","~ ","\\? ","~ ","hurt","hUWUrt","kill","hwuwrt","you","you<3","r","w","l","w","uwu","UWU","owo","OWO",";-;","(-_-)","-_-","(-_-)",":o","※(^o^)/※",":0","※(^o^)/※",":\\)","(｡◕‿‿◕｡)",":>","(｡◕‿‿◕｡)",":\\(","(︶︹︶)",":<","(︶︹︶)",":3","(・3・)",":D","(ﾉ◕ヮ◕)ﾉ*:・ﾟ✧","\\._\\.","(っ´ω`c)","fuck","fwick","shit","*poops*","wtf","whawt the fwick","wth","whawt the hecc"]

a = 0
b = ""

for e in i:
    a += 1
    if a == 3:
        a = 1
    if a == 1:
        b="uwuText.replace(\"" + e + "\", \""
    if a == 2:
        print(b + e, "\");")
