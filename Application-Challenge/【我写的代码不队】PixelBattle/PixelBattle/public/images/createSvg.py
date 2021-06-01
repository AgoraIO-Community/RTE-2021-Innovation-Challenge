import random
f = open('./img2.svg', 'w')
f.write('<?xml version=\''+'1.0\''+' encoding='+'\'utf-8\''+'?><svg xmlns="http://www.w3.org/2000/svg" version="1.1" id="Layer_1" x="0px" y="0px" viewBox="0 0 400 400" enable-background="new 0 0 400 400">')
for i in range(401):
	for j in range(401):
		f.write('<rect x="'+str(i)+'" y="'+str(j)+'" width="1" height="1" style="fill:rgb('+str(random.randint(0,25))+','+str(random.randint(0,25))+','+str(random.randint(0,25))+') "/>')
f.write('</svg>')
f.close()