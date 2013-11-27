#Scale image handle class
import glob, os
import cStringIO
import wx
from pil2image import *


class CompareHandle:

    def __init__(self):
        self.doneNum = 0;
        self.totalNum = 0;

    def CompareImage(self, gui, ratio, leftDir, rightDir):
        if leftDir == rightDir :
            return 1
        if os.path.exists(leftDir) == False :
            os.makedirs(leftDir)
        if os.path.exists(rightDir) == False :
            os.makedirs(rightDir)
        self.leftDir = leftDir
        self.rightDir = rightDir
        self.gui = gui
        self.ratio = ratio
        return 0

    def CompareHandle(self):
        self.doneNum = 0;
        self.totalNum = 0;
        leftlist = glob.glob(self.leftDir + "/*.png")
        rightlist = glob.glob(self.rightDir + "/*.png")
        list = []
        for infile in leftlist:
            dirt, file = os.path.split(infile)
            list.append(file)
        for infile in rightlist:
            dirt, file = os.path.split(infile)
            if file not in list :
                list.append(file)
        self.totalNum = len(list)
        list.sort()
        for infile in list:
            try:
                data = open(self.leftDir + "/" + infile, "rb").read()
                stream1 = cStringIO.StringIO(data)
            except Exception:
                self.gui.Miss(infile, "Miss", "Ok", "-", "-")
                continue
            try:
                data = open(self.rightDir + "/" + infile, "rb").read()
                stream2 = cStringIO.StringIO(data)
            except Exception:
                self.gui.Miss(infile, "Ok", "Miss", "-", "-")
                continue
            img1 = wx.ImageFromStream(stream1, wx.BITMAP_TYPE_PNG)
            img2 = wx.ImageFromStream(stream2, wx.BITMAP_TYPE_PNG)
            img1W = img1.GetWidth()
            img1H = img1.GetHeight()
            img2W = img2.GetWidth()
            img2H = img2.GetHeight()
            a = img1W*self.ratio
            img1WScaled = int(float("%.0f" % a))
            a = img1H*self.ratio
            img1HScaled = int(float("%.0f" % a))
            if img1WScaled == img2W and img1HScaled == img2H :
                self.gui.Pass(infile, "-", "Pass", str(self.ratio), str(self.ratio))
            elif img1WScaled == img2W :
                self.gui.Error(infile, "-", "Error", str(self.ratio), str(float(img2H)/img1H))
            elif img1HScaled == img2H :
                self.gui.Error(infile, "-", "Error", str(float(img2W)/img1W), str(self.ratio))
            else :
                self.gui.Error(infile, "-", "Error", str(float(img2W)/img1W), str(float(img2H)/img1H))
        self.gui.processDone(self.totalNum)

    def GetImage(self, imageName) :
        images = []
        try:
            data = open(self.leftDir + "/" + imageName, "rb").read()
            stream1 = cStringIO.StringIO(data)
            img1 = wx.ImageFromStream(stream1, wx.BITMAP_TYPE_PNG).ConvertToBitmap()
            images.append(img1)
        except Exception:
            print "left dir no image file"
        try:
            data = open(self.rightDir + "/" + imageName, "rb").read()
            stream2 = cStringIO.StringIO(data)
            img2 = wx.ImageFromStream(stream2, wx.BITMAP_TYPE_PNG).ConvertToBitmap()
            images.append(img2)
        except Exception:
            print "right dir no image file"
        return images

    def GetNum(self):
        return self.doneNum

    def GetTotal(self):
        return self.totalNum



