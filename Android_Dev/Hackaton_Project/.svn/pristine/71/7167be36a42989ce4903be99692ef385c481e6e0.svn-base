#Scale image handle class
import wx
import glob, os
from PIL import Image
from pil2image import *


class ScaleHandle:

    def __init__(self):
        self.doneNum = 0;
        self.totalNum = 0;

    def ScaleImage(self, dialog, ratio, inputDir, outputDir):
        print "input dir: "+inputDir
        print "output dir: "+outputDir
        if inputDir == outputDir :
            return 1
        if os.path.exists(inputDir) == False :
            os.makedirs(inputDir)
        if os.path.exists(outputDir) == False :
            os.makedirs(outputDir)
        self.inputDir = inputDir
        self.outputDir = outputDir
        self.dialog = dialog
        self.ratio = ratio
        return 0

    def ScaleHandle(self):
        self.doneNum = 0;
        self.totalNum = 0;
        list = glob.glob(self.inputDir + "/*.png")
        self.totalNum = len(list)
        for infile in list:
            img = wx.Image(infile, wx.BITMAP_TYPE_PNG)
            dirt, file = os.path.split(infile)
            imgW = img.GetWidth()
            imgH = img.GetHeight()
            a = imgW*self.ratio
            imgW = int(float("%.0f" % a))
            a = imgH*self.ratio
            imgH = int(float("%.0f" % a))
            size = imgW, imgH
            if imgW <= 0 or imgH <= 0:
                self.dialog.failProcess(file)
                self.dialog.updateProcess(file + " --- Fail")
                continue
            else:
                #scaledImg = img.Scale(imgW,imgH, wx.IMAGE_QUALITY_HIGH)
                #scaledImg.SaveFile(os.path.join(self.outputDir, file), wx.BITMAP_TYPE_PNG)

                im = WxImageToPilImage(img, file)
                im.thumbnail(size, Image.ANTIALIAS)
                im.save(os.path.join(self.outputDir, file), optimize=1)
                self.dialog.updateProcess(file)
                self.doneNum += 1
        self.dialog.updateProcess("----------------------\n")
        self.dialog.updateProcess(str(self.totalNum) + " images done !")
        self.dialog.updateProcess(str(self.doneNum) + " images success !")
        self.dialog.updateProcess(str(self.totalNum - self.doneNum) 
                + " images failure !")
        if self.totalNum > self.doneNum:
            self.dialog.updateProcess("----------------------\n")
            self.dialog.updateProcess("Failure Cause:\n"
                    + "The images width or height multiply by ratio <= 0") 
        self.dialog.processDone()

    def GetNum(self):
        return self.doneNum

    def GetTotal(self):
        return self.totalNum


