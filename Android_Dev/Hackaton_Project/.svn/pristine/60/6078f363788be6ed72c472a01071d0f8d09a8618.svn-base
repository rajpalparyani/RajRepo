import os
import shutil
from wax import *
from compareHandle import *


class CompareImagePanel(VerticalPanel):

    def __init__(self, *args, **kwargs):
        VerticalPanel.__init__(self, *args, **kwargs)

        self.Load()
        parent = self.GetParent()
        width = parent.GetSize()[0]
        height = parent.GetSize()[1]
        height = height/5
        width = width/2


        # use a panel for better layout of the dir setting
        panel = Panel(self)
        # setting input dir
        p1 = VerticalPanel(panel)
        label = Label(p1, "Reference:")
        p1.AddComponent(label)
        self.inputPath = TextBox(p1, self.path)
        self.inputPath.SizeX = 200
        p1.AddComponent(self.inputPath, border=2)
        b = Button(p1, "Choose", event=self.ChooseInputDirectory)
        p1.AddComponent(b, border=3)
        p1.AddSpace(0, height/2)
        label = Label(p1, "Reference screen size:")
        p1.AddComponent(label, border=2)
        self.screenSize1 = ComboBox(p1, readonly=1)
        p1.AddComponent(self.screenSize1, border=2)
        p1.Pack()

        ##setting output dir
        p2 = VerticalPanel(panel)
        label = Label(p2, "Target:")
        p2.AddComponent(label)
        self.outputPath = TextBox(p2, self.path)
        self.outputPath.SizeX = 200
        p2.AddComponent(self.outputPath, border=2)
        b = Button(p2, "Choose", event=self.ChooseOutputDirectory)
        p2.AddComponent(b, border=3)
        p2.AddSpace(0, height/2)
        label = Label(p2, "Target screen size:")
        p2.AddComponent(label, border=2)
        self.screenSize2 = ComboBox(p2, readonly=1)
        p2.AddComponent(self.screenSize2, border=2)
        p2.Pack()

        panel.AddSpace(width/4,0)
        panel.AddComponent(p1)
        panel.AddSpace(width/4,0)
        panel.AddComponent(p2)
        panel.Pack()

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
        panelFilter = Panel(self)
        label = Label(panelFilter, "Filter:")
        panelFilter.AddComponent(label, border=5)
        comboBoxItems = ["Show Error and Miss", "Show Error", "Show All"]
        self.comboBox = ComboBox(panelFilter, comboBoxItems, readonly=1)
        self.comboBox.SetSelection(0)
        panelFilter.AddComponent(self.comboBox, align='center', expand='h', border=2)
        panelFilter.Pack()

        #bottom buttons
        panelBottom = Panel(self)
        self.start = Button(panelBottom, "Compare", event=self.Start)
        panelBottom.AddComponent(self.start, border=5)
        self.exit = Button(panelBottom, "Exit")
        panelBottom.AddComponent(self.exit, border=5)
        panelBottom.Pack()


        bgColor = panelBottom.GetBackgroundColour()
        self.SetBackgroundColour(bgColor)


        self.AddSpace(0, height/2)
        self.AddComponent(panel)
        self.AddSpace(0, height/2)
        self.AddComponent(panelRatio, align='center')
        self.AddSpace(0, height/2)
        self.AddComponent(panelFilter, align='center')
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
        self.filter = self.comboBox.GetSelection()
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
        result = self.handle.CompareImage(self.processDialog, ratio,
                self.inputPath.GetValue(), 
                self.outputPath.GetValue())
                #"E:\\TN70\\mobile\\apps\\trunk\\telenav-global\\res\\android\\cingular\\i18n\\generic\\images\\medium", 
                #"E:\\TN70\\mobile\\apps\\trunk\\telenav-global\\res\\android\\cingular\\i18n\\generic\\images\\huge")
                #"D:\\testspace\\Pytest\\imageTools\\input",
                #"D:\\testspace\\Pytest\\imageTools\\output")
        if result == 0 :
            self.start.Enable(False)
            self.exit.Enable(False)
            self.processDialog.initialization()
            self.processDialog.Show()
            self.handle.CompareHandle()
        elif result == 1:
            msg = MessageDialog(self, "Message", 
                    "Warning: reference and target dirs are the same one!"
                    +"\nPlease make they are different!")
            msg.ShowModal()
            msg.Destroy()

    def SetScreenSize(self, screen_size):
        for size in screen_size:
            try:
                s = size[0].strip() + " x " + size[1].strip() + " " + size[2].strip()
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
        self.handle = CompareHandle()
        self.processDialog = ProcessFrame(self)
        self.inputRatioEnable = False
        self.filter = 0


class CompareListView(ListView):
    def __init__(self, parent, **rest):
        ListView.__init__(self, parent, 
                columns=["id", "Image Name", "Refer", "Target", "Ratio of width", "Ratio of height"], 
                rules='both', **rest)
        self.SetSize([640, 480])
        width = self.GetSize()[0]
        self.SetColumnWidth(0, 40)
        self.SetColumnWidth(1, width*4/8)
        self.SetColumnWidth(2, width/16)
        self.SetColumnWidth(3, width/16)
        self.SetColumnWidth(4, width*3/16 -20)
        self.SetColumnWidth(5, width*3/16 -20)
        self.itemCount = 0


class ProcessFrame(VerticalFrame):
    def __init__(self, *args, **kwargs):
        VerticalFrame.__init__(self, *args, **kwargs)
        self.load()
        self.SetLabel("Compare Result")
        self.listview = CompareListView(self)
        self.listview.OnItemDoubleClick = self.OnListItemDoubleClick
        self.AddComponent(self.listview, expand='b')

        stat = self.statistics(self)
        self.AddComponent(stat, expand='h')
        toolbar = self.make_toolbar(self)
        self.AddComponent(toolbar, expand='h')

        self.Pack()

    def load(self):
        self.errorImages = []
        self.total = 0
        self.passNum = 0
        self.errorNum = 0
        self.missNum = 0

    def make_toolbar(self, parent):
        p = HorizontalPanel(parent)
        width = parent.GetSize()[0]
        p.AddSpace(width/2,0)
        b = Button(p, "Export error images of target", event=self.export)
        p.AddComponent(b, align="center")
        b = Button(p, "Exit", event=self.over)
        p.AddComponent(b, align="center")
        p.Pack()
        return p

    def statistics(self, parent):
        p = HorizontalPanel(parent)
        width = parent.GetSize()[0]
        self.totalLabel = Label(p, "")
        p.AddComponent(self.totalLabel, align="left")
        p.AddSpace(width/3,0)
        self.passLabel = Label(p, "")
        p.AddComponent(self.passLabel, align="left")
        p.AddSpace(width/3,0)
        self.errorLabel = Label(p, "")
        p.AddComponent(self.errorLabel, align="left")
        p.AddSpace(width/3,0)
        self.missLabel = Label(p, "")
        p.AddComponent(self.missLabel, align="left")
        p.Pack()
        return p
         

    def Pass(self, imageName, left, right, ratioW, ratioH):
        self.passNum += 1
        if self.GetParent().filter == 2 :
            index = self.insertItem(imageName, left, right, ratioW, ratioH)
            item = self.listview.GetItem(index)
            item.SetBackgroundColour("green")
            self.listview.SetItem(item)

    def Error(self, imageName, left, right, ratioW, ratioH):
        self.errorNum += 1
        self.errorImages.append(imageName)
        index = self.insertItem(imageName, left, right, ratioW, ratioH)
        item = self.listview.GetItem(index)
        item.SetTextColour("white")
        item.SetBackgroundColour("red")
        self.listview.SetItem(item)

    def Miss(self, imageName, left, right, ratioW, ratioH):
        self.missNum += 1
        if self.GetParent().filter != 1 :
            index = self.insertItem(imageName, left, right, ratioW, ratioH)
            item = self.listview.GetItem(index)
            item.SetBackgroundColour("yellow")
            self.listview.SetItem(item)

    def insertItem(self, imageName, left, right, ratioW, ratioH):
        index = self.listview.itemCount
        self.listview.InsertStringItem(index, '')
        self.listview.SetStringItem(index, 0, str(index))
        self.listview.SetStringItem(index, 1, imageName)
        self.listview.SetStringItem(index, 2, left)
        self.listview.SetStringItem(index, 3, right)
        self.listview.SetStringItem(index, 4, ratioW)
        self.listview.SetStringItem(index, 5, ratioH)
        self.listview.itemCount += 1
        return index


    def OnListItemDoubleClick(self, event=None):
        self.listview = event.GetEventObject()
        currentitem = event.m_itemIndex
        itemName = self.listview.GetStringItem(currentitem,1)
        imageView = Frame(self)
        imageView.SetLabel(itemName)
        images = self.GetParent().handle.GetImage(itemName)
        for img in images:
            bmp = Bitmap(imageView, img)
            imageView.AddComponent(bmp)
        imageView.Pack()
        imageView.Show()


    def processDone(self, number):
        self.total = number
        self.GetParent().start.Enable(True)
        self.GetParent().exit.Enable(True)
        self.totalLabel.SetLabel(" " + str(self.total) + " images compared")
        self.passLabel.SetLabel(" " + str(self.passNum) + " images pass")
        self.errorLabel.SetLabel(" " + str(self.errorNum) + " images error")
        self.missLabel.SetLabel(" " + str(self.missNum) + " images miss")

    def initialization(self):
        self.load()
        self.listview.DeleteAllItems()
        self.listview.itemCount = 0

    def export(self, event=None):
        input = self.GetParent().outputPath.GetValue()
        current = os.getcwd() + "\\compare_" + self.GetParent().screenSize2.GetStringSelection().replace(':', '_')
        if os.path.exists(current) == False :
            os.makedirs(current)
        else :
            shutil.rmtree(current)
            os.makedirs(current)
        for file in self.errorImages:
            shutil.copy2(input + "\\" + file, current)
        os.system('explorer '+current)

    def over(self, event=None):
        self.Hide()

