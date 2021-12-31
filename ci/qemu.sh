#!/bin/sh
# __________________________________________________________________________
# Collection of documentation on how to setup a qemu based build environment
# ==========================================================================
#
# [ directory ci/qemu is in .gitignore ]
#
#
# http://www.redfelineninja.org.uk/daniel/2018/02/running-an-iso-installer-image-for-arm64-aarch64-using-qemu-and-kvm/
#    Load CD-ROM drivers from removable media?: Select No
#    Manually select a CD-ROM module and device?: Select Yes
#    Module needed for accessing the CD-ROM: Select none
#    Device file for accessing the CD-ROM: Enter /dev/vdb and press Continue
#
# http://phwl.org/2021/qemu-armhf-debian/
# https://futurewei-cloud.github.io/ARM-Datacenter/qemu/how-to-launch-aarch64-vm/
#
#    user: arm
#    pwd:  arm64
#
# create images:
#    mkdir qemu
#    qemu-img create -f qemu/qcow2 debian-aarch64.qcow2 64G
#    qemu-img create -f qemu/qcow2 varstore.img 64M
#
# start the virtual machine:
qemu-system-aarch64 \
    -cpu cortex-a53 -M virt -m 8G -nographic \
    -drive if=pflash,format=raw,file=qemu/QEMU_EFI.img \
    -drive if=pflash,file=qemu/varstore.img \
    -drive if=virtio,file=qemu/debian-aarch64.qcow2 \
    -drive if=virtio,format=raw,file=qemu/debian-testing-arm64-netinst.iso \
    -net nic,model=virtio \
    -net user,hostfwd=tcp::2222-:22

# install packages:
#    see .github/workflows
