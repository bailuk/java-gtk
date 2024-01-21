# Development environment with nix-shell
{ pkgs ? import <nixpkgs> {}
}:
pkgs.mkShell {
    name="java-gtk";
    buildInputs = [
        pkgs.git
        pkgs.jdk21_headless # Non headless is linked against gtk3 and does therefore not work
        pkgs.gtk4
    ];
    shellHook = ''
        LD_LIBRARY_PATH=${pkgs.gtk4.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.glib.out.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.gdk-pixbuf.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.cairo.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.pango.out.outPath}/lib
        LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${pkgs.libadwaita.outPath}/lib
        export LD_LIBRARY_PATH
        echo "./gradlew generate && ./gradlew build && ./gradlew run"
    '';
}
