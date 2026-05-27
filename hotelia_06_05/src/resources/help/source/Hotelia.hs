
<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
                         "http://java.sun.com/products/javahelp/helpset_2_0.dtd">

<helpset version="2.0">
    <title>Hotelia - Sistema de Gestión Hotelera</title>
    
    <maps>
        <homeID>index</homeID>
        <mapref location="HC.xml" />
    </maps>
    
    <view>
        <name>Tabla de Contenidos</name>
        <label>Tabla de Contenidos</label>
        <type>javax.help.TOCView</type>
        <data>TOC.xml</data>
    </view>
    
    <view>
        <name>Índice</name>
        <label>Índice</label>
        <type>javax.help.IndexView</type>
        <data>Index.xml</data>
    </view>
    
    <view>
        <name>Buscar</name>
        <label>Buscar</label>
        <type>javax.help.SearchView</type>
        <data>JavaHelpSearch</data>
    </view>
    
    <view>
        <name>Favoritos</name>
        <label>Favoritos</label>
        <type>javax.help.FavoritesView</type>
    </view>
    
    <presentation default="true" displayviews="true" displayviewimages="true">
        <name>Ventana Principal de Ayuda</name>
        <size width="750" height="600" />
        <location x="200" y="200" />
    </presentation>
</helpset>