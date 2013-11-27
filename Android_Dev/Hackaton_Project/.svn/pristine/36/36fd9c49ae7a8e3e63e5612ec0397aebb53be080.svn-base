import os
import shutil
from wax import *
from scaleHandle import *


class ScaleImagePanel(VerticalPanel):

    def __init__(self, *args, **kwargs):
        VerticalPanel.__init__(self, *args, **kwargs)

        self.Load()

        # use a panel for better layout of the dir setting
        # setting input dir
        p1 = Panel(self)
        label = Label(p1, "Input :")
        p1.AddComponent(label, border=5)
        self.inputPath = TextBox(p1, self.path)
        self.inputPath.SizeX = 300
        p1.AddComponent(self.inputPath, border=2)
        b = Button(p1, "Choose", event=self.ChooseInputDirectory)
        p1.AddComponent(b, border=3)
        p1.Pack()

        panel1 = Panel(self)
        label = Label(panel1, "Input screen size:")
        panel1.AddComponent(label, border=5)
        self.screenSize1 = ComboBox(panel1, readonly=1)
        panel1.AddComponent(self.screenSize1, align='center', expand='h', border=2)
        panel1.Pack()

        #setting output dir
        p2 = Panel(self)
        label = Label(p2, "Output :")
        p2.AddComponent(label, border=5)
        self.outputPath = TextBox(p2, self.path)
        self.outputPath.SizeX = 300
        p2.AddComponent(self.outputPath, align='center', expand='h', border=2)
        b = Button(p2, "Choose", event=self.ChooseOutputDirectory)
        p2.AddComponent(b, border=3)
        p2.Pack()

        panel2 = Panel(self)
        label = Label(panel2, "Output screen size:")
        panel2.AddComponent(label, border=5)
        self.screenSize2 = ComboBox(panel2, readonly=1)
        panel2.AddComponent(self.screenSize2, align='center', expand='h', border=2)
        panel2.Pack()

        #setting ratio
        panelRatio = Panel(self)
        self.cb = CheckBox(panelRatio, "Set Ratio")
        self.cb.OnCheck = self.OnSetRatio
        panelRatio.AddComponent(self.cb, border=5)
        self.ratioText = TextBox(panelRatio, "1.0", justify="right")
        self.ratioText.Enable(False)
        panelRatio.AddComponent(self.ratioText, border=2)
        panelRatio.Pack()

        #setting image format
        panelImgFormat = Panel(self)
        label = Label(panelImgFormat, "Transform Image Format:")
        panelImgFormat.AddComponent(label, border=5)
        comboBoxItems = ["png -> png", "jpg -> png", "bmp -> png"]
        self.comboBox = ComboBox(panelImgFormat, comboBoxItems, readonly=1)
        self.comboBox.SetSelection(0)
        self.comboBox.Enable(False)
        panelImgFormat.AddComponent(self.comboBox, align='center', expand='h', border=2)
        panelImgFormat.Pack()


        #bottom buttons
        panelBottom = Panel(self)
        self.start = Button(panelBottom, "Run", event=self.Start)
        panelBottom.AddComponent(self.start, border=5)
        self.exit = Button(panelBottom, "Exit")
        panelBottom.AddComponent(self.exit, border=5)
        panelBottom.Pack()


        bgColor = panelBottom.GetBackgroundColour()
        self.SetBackgroundColour(bgColor)


        parent = self.GetParent()
        height = parent.GetSize()[1]
        height = height/5

        self.AddSpace(0, height/2)
        self.AddComponent(p1, align='center')
        self.AddComponent(panel1, align='center')
        self.AddSpace(0, height/2)
        self.AddComponent(p2, align='center')
        self.AddComponent(panel2, align='center')
        self.AddSpace(0, height/2)
        self.AddComponent(panelRatio, align='center')
        self.AddSpace(0, height/2)
        self.AddComponent(panelImgFormat, align='center')
        self.AddSpace(0, height/2)
        self.AddComponent(panelBottom, align='center')

        self.Pack()

    def ChooseInputDirectory(self, event):
        dirdialog = DirectoryDialog(self, "Choose a directory")
        dirdialog.SetPath(self.inputPath.GetValue())
        dirdialog.ShowModal()
        dirdialog.Destroy()
        path = dirdialog.GetPath()
        if path != None :
            self.inputPath.SetValue(path)

    def ChooseOutputDirectory(self, event):
        dirdialog = DirectoryDialog(self, "Choose a directory")
        dirdialog.SetPath(self.outputPath.GetValue())
        dirdialog.ShowModal()
        dirdialog.Destroy()
        path = dirdialog.GetPath()
        if path != None :
            self.outputPath.SetValue(path)

    def OnSetRatio(self, event):
        if event.GetEventObject().IsChecked():
            self.screenSize1.Enable(False)
            self.screenSize2.Enable(False)
            self.ratioText.Enable(True)
            self.inputRatioEnable = True
        else:
            self.screenSize1.Enable(True)
            self.screenSize2.Enable(True)
            self.ratioText.Enable(False)
            self.inputRatioEnable = False

    def Start(self, event=None):
        if self.inputRatioEnable :
            value = self.ratioText.GetValue().strip()
            if len(value) == 0 :
                msg = MessageDialog(self, "Message", 
                        "Please input ratio value!")
                msg.ShowModal()
                msg.Destroy()
                return
            ratio = float(value)
        else :
            index1 = self.screenSize1.GetSelection()
            index2 = self.screenSize2.GetSelection()
            size1 = min(int(self.screenSize[index1][0]), int(self.screenSize[index1][1]))
            size2 = min(int(self.screenSize[index2][0]), int(self.screenSize[index2][1]))
            print size1
            print size2
            a = float(size2)/size1
            ratioH = float("%.2f" % a)
            print ratioH
            size1 = max(int(self.screenSize[index1][0]), int(self.screenSize[index1][1]))
            size2 = max(int(self.screenSize[index2][0]), int(self.screenSize[index2][1]))
            print size1
            print size2
            a = float(size2)/size1
            ratioW = float("%.2f" % a)
            print ratioW
            ratio = (ratioW + ratioH) / 2
        print ratio
        result = self.handle.ScaleImage(self.processDialog, ratio,
                self.inputPath.GetValue(), 
                self.outputPath.GetValue())
        if result == 0 :
            self.start.Enable(False)
            self.exit.Enable(False)
            self.processDialog.initialization()
            self.processDialog.Show()
            self.handle.ScaleHandle()
        elif result == 1:
            msg = MessageDialog(self, "Message", 
                    "Warning: input and output dirs are the same one!"
                    +"\nPlease make they are different!")
            msg.ShowModal()
            msg.Destroy()

    def SetScreenSize(self, screen_size):
        for size in screen_size:
            try:
                s = size[0] + " x " + size[1] + " " + size[2].strip()
                self.screenSize1.Append(s)
                self.screenSize2.Append(s)
            except Exception:
                continue
        self.screenSize1.SetSelection(0)
        self.screenSize2.SetSelection(0)
        self.screenSize = screen_size


    def SetExitHandler(self, event=None):
        self.exit.OnClick = event


    def Load(self):
        self.path = os.getcwd()
        self.handle = ScaleHandle()
        self.processDialog = ProcessFrame(self)
        self.inputRatioEnable = False

class ProcessFrame(VerticalFrame):
    def __init__(self, *args, **kwargs):
        VerticalFrame.__init__(self, *args, **kwargs)

        self.failImages = []
        self.SetLabel("Scale Result")
        self.textbox = TextBox(self, multiline=1, readonly=1,
                       Font=Font("Courier New", 10), Size=(640,480),
                       Value="")
        self.AddComponent(self.textbox, expand='both')

        toolbar = self.make_toolbar(self)
        self.AddComponent(toolbar, expand='h')

        self.Pack()

    def make_toolbar(self, parent):
        p = HorizontalPanel(parent)
        width = parent.GetSize()[0]
        p.AddSpace(width/2,0)
        b = Button(p, "Export fail images", event=self.export)
        p.AddComponent(b, align="center")
        b = Button(p, "Exit", event=self.over)
        p.AddComponent(b, align="center")
        p.Pack()
        return p

    def updateProcess(self, text):
        self.textbox.AppendText(text)
        self.textbox.AppendText("\n")

    def failProcess(self, text):
        self.failImages.append(text)

    def processDone(self):
        self.GetParent().start.Enable(True)
        self.GetParent().exit.Enable(True)

    def initialization(self):
        self.failImages = []
        self.textbox.Clear()

    def export(self, event=None):
        input = self.GetParent().inputPath.GetValue()
        current = os.getcwd() + "\\scale_" + self.GetParent().screenSize1.GetStringSelection().replace(':', '_')
        if os.path.exists(current) == False :
            os.makedirs(current)
        else :
            shutil.rmtree(current)
            os.makedirs(current)
        for file in self.failImages:
            shutil.copy2(input + "/" + file, current)
        os.system('explorer '+current)

    def over(self, event=None):
        self.Hide()

