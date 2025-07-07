{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {

  packages = with pkgs;
  [
  jdk17
  ];

  shellHook = ''
    export LD_LIBRARY_PATH="''${LD_LIBRARY_PATH}''${LD_LIBRARY_PATH:+:}${pkgs.libglvnd}/lib"
  '';

}
