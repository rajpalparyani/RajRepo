SCREEN_SIZE = [['800','480','(5:3)'], ['480','320','(3:2)']]

def GetScreenSize():
    screen_size = SCREEN_SIZE
    try:
        file = open("screen_size.txt", "r")
    except Exception:
        return screen_size
    while 1:
        line = file.readline()       # read line by line
        if not line: break
        try:
            element = line.split(",")
            element[0] = element[0].strip()
            element[1] = element[1].strip()
            element[2] = element[2].strip()
        except Exception:
            continue
        if element not in screen_size:
            screen_size.append(element)
    file.close()
    return screen_size
