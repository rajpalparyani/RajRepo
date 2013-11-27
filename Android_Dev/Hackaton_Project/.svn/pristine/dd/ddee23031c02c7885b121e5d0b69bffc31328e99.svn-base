from wax import *

class CutImagePanel(VerticalPanel):
    def __init__(self, *args, **kwargs):
        VerticalPanel.__init__(self, *args, **kwargs)

        lbl = Label(self, text='Coming Soon')


        parent = self.GetParent()
        height = parent.GetSize()[1]
        height = height/3
        self.AddSpace(0, height)
        self.AddComponent(lbl, expand='h', align="center")

        self.Pack()

