rmdir -r austinaudio
set PATH_TO_FX=C:\Program Files\Java\javafx-sdk-11.0.2\lib
set PATH_TO_FX_MODS=C:\Program Files\Java\javafx-jmods-11.0.2
dir /s /b src\*.java > sources.txt & javac --module-path "%PATH_TO_FX%" -d mods/austinaudio @sources.txt & del sources.txt
jlink --module-path "%PATH_TO_FX_MODS%;mods" --add-modules austinaudio --output austinaudio
pause