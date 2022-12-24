
# UT3 Converter2 Readme file

<b>UT3 Converter is now longer being developped and maintained.
Use [UT4X converter](https://github.com/xtremexp/UT4X-Converter) instead</b>

Version: 0.26 - Final
* Author: XtremeXp
* Release Date: 03/01/2020
* Status: <b>Discontinued</b>
* Web page: https://www.epicgames.com/unrealtournament/forums/past-unreal-tournament-games/unreal-tournament-3/407842
* Forum : https://www.epicgames.com/unrealtournament/forums/past-unreal-tournament-games/unreal-tournament-3/407842


---

# Description
UT3 Converter 2 hels level designers saving a lot of time while
converting/porting UT maps. This tool automates some task
for conversion which means conversion of level ressources
like textures,staticmeshes and actors.

---

# UTx conversions supported
------------------------------
UT3 Converter 2 supports these conversions:
* Unreal Tournament -> Unreal Tournament 2004
* Unreal Tournament -> Unreal Tournament 3
* Unreal Tournament 2004-> Unreal Tournament 3

---

# Requirements
You need:
* Two of the following UT games:
    * Unreal Tournament
    * Unreal Tournament 2004
    * Unreal Tournament 3
* Java technology installed (see www.java.com)
* Some skills with UnrealEd software

---

# Installation and first start
* Decompress the ZIP archive to any folder of your choice.
* Double-click on UT3Converter2.jar.
* If not working install java (https://www.java.com/) or create some "run.bat" file with notepad that you will save in the root program folder with this in it
> java -jar ut3converter2.jar 
* this will force launching this file associating it as java program

---

# What this program converts
* Textures
* Brushes
* Staticmeshes
* Weapons
* Pickups
* Most of main actors

--- 

# What this program DOES NOT converts
* Custom scripts
* Special textures (shaders,pantex,...)
---

# Limitation/Issues/Known bugs
* All - Do not work with folders with spaces (e.g.: "C:\Program Files\UnrealTournament")
* UT and DeusEx Games - Will not convert custom textures in external packages (not in myLevel/e.g: ../Textures/myPackage.utx) for 
* UT2004->UT3 - Some staticmeshes can't be converted. ("ucc batchexport mypackage.usx StaticMesh t3d" cmd bug)
* Can't convert maps using UTF16-LE or UTF16-BE t3d file encoding. Check that mapname doesn't have exotic chars such as {%^,.. but only alphanumeric values (a->z,0->9) then save your map.

---

# Origin of the project
As a mapper with mainly UT2004 level editor (UnrealEd) and as
UT3 was released i wanted to port some of my maps to UT3 but
didn't suceed, that's why i started digging into Unreal Technology
concept

---

# Additional Credits
Tools:

    * Nvconvert - Pierre-E Gougelet - dds to psd conversion http://www.xnview.com/
    * UED Texture Toolkit - Alex Stewart - Terrain HeightMap 16-bit greyscale texture conversion (.bmp->.tiff) - http://www.foogod.com/UEdTexKit/
    * Sox - Chris Bagwell -Wav conversion - http://sox.sourceforge.net
    * UT Packages Delphi unit - Antonio Cordero Balcázar - http://www.acordero.org


Librairies:

    * JAI - Java Advanced Imaging - .tiff image reader - https://jai.dev.java.net/ -jai_codec.jar,jai_core.jar
    * Java 3D Vecmath - For handling brushes - https://vecmath.dev.java.net/ -vecmath.jar
    * JOGL - Java OpenGL - DDS Reader - https://jogl.dev.java.net/ -jogl.jar
    * SwingX - Java Advanced Interface https://swingx.dev.java.net/ - swingx-2008_09_13.jar
    * NimROD Look and Feel -Theme Interface http://personales.ya.com/nimrod/index-en.html -nimrodlf-1.0e.jar
	
 Users at Epic forums for reporting bugs and supporting this project<br>
.:..: for helping me with UT to UT2004 basic conversion concepts
