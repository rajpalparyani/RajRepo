import wx
from PIL import *

def pil2image(pil,alpha=True):
    """Convert PIL Image to wx.Image."""
    if alpha:
        image = apply( wx.EmptyImage, pil.size )
        image.SetData( pil.convert( "RGB").tostring() )
        image.SetAlphaData(pil.convert("RGBA").tostring()[3::4])
    else:
        image = wx.EmptyImage(pil.size[0], pil.size[1])
        new_image = pil.convert('RGB')
        data = new_image.tostring()
        image.SetData(data)
    return image

def image2pil(image):
    """Convert wx.Image to PIL Image."""
    pil = Image.new('RGBA', (image.GetWidth(), image.GetHeight()))
    pil.fromstring(image.GetData())
    return pil


def WxImageToPilImage( wxImage, file, wantAlpha=True ):   # 2 ==> 3  Assume keeping any alpha channel

    pilImage = Image.new( 'RGB', (wxImage.GetWidth(), wxImage.GetHeight()), color=(255, 255, 255) )
    pilImage.fromstring( wxImage.GetData() )    # RGB data

    if wantAlpha :       # Convert and insert the wxImage alpha channel
        if not wxImage.HasAlpha() :
            wxImage.InitAlpha()

        pilImage.convert( 'RGBA' )              # add an alpha channel with dummy values
        wxAlphaStr = wxImage.GetAlphaData()     # extract the wxImage alpha channel in "string" format

        # Create a single band(Pil)/channel(wx) dummy image that is the dimensions of the wxImage.
        pilAlphaImage = Image.new( 'L', (wxImage.GetWidth(), wxImage.GetHeight()) )

        # Insert the wx alpha data string into the new Pil image.
        pilAlphaImage = Image.fromstring( 'L', (wxImage.GetWidth(), wxImage.GetHeight()), wxAlphaStr )

        # Copy the single-band Pil alpha-data image into the alpha channel of the target RGBA image.
        pilImage.putalpha( pilAlphaImage )      # all done !
        
    return pilImage

#end def


def PilImageToWxImage( pilImage, wantAlpha=True, createAlpha=False ) :     # 3 ==> 2

    # If the pilImage has alpha, then automatically preserve it in the wxImage
    #   unless alpha preservation is disabled by setting wantAlpha=False.
    #
    # If the pilImage mode is RGB, the optionally create a new wxImage alpha plane/band
    #   by setting createAlpha=True.
    #
    if (pilImage.mode == 'RGBA') :       # ignore createAlpha since mode=RGBA

        # Extract the existing alpha band from the the wxImage.
        wxImage = wx.EmptyImage( *pilImage.size  )

        # Pil's creator of "in-place" methods ought to be horse-whipped !
        pilRgbImage = pilImage      # Do NOT let Pil's in-place conversion alter the original image !
        pilRgbDataStr = pilRgbImage.convert( 'RGB' ).tostring()   #"In-place" method destroys the original !!
        wxImage.SetData( pilRgbDataStr )    # Just the RGB data from the new RGB mode image.

        if wantAlpha :
            # Copy just the pilImage alpha into wxImage alpha plane.
            pilAlphaStr = pilImage.tostring()
            pilAlphaStr = pilAlphaStr[3::4]
            wxImage.SetAlphaData( pilAlphaStr )
        #end if

    elif (pilImage.mode == 'RGB') :     # RGB mode pilImage RGB planes ==> wxImage

        wxImage = wx.EmptyImage( *pilImage.size  )
        wxImage.SetData( pilImage.tostring() )

        if createAlpha :                # Create a brand new alpha plane/band/layer/channel
            # Create and insert 100% opaque alpha (values=255)
            # .convert( 'RGBA' ) always adds a brand new 100% opaque pilImage alpha plane.
            # .SetAlphaData() adds a brand new wxImage alpha plane in this case
            #   since the wxImage doesn't originally have any alpha plane.
            pilRgbaImage = pilImage     # Do NOT let Pil's in-place conversion alter the original image !

            wxImage.SetAlphaData( pilRgbaImage.convert( 'RGBA' ).tostring()[3::4] )
        #end if

    #end if

    return wxImage      # May or may not have an alpha plane depending on the input image mode
                        #   and the given option flags.

#end def

#WxImageToPilImage & PilImageToWxImage
#Reference url:http://permalink.gmane.org/gmane.comp.python.wxpython/79316
