import {Component, HostListener, OnInit} from '@angular/core';
import {AddressService} from "../service/address.service";
import {UserService} from "../service/user.service";
import {MessageService} from "primeng/api";
import {Router} from "@angular/router";

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.scss']
})
export class AddressComponent implements OnInit{
  provinces: any[] = [];
  districts: any[] = [];
  wards: any[] = [];
  addAddressRequest: any = {};
  selectedProvinceId: any = null;
  selectedDistrictId: any = null;
  selectedWardId: any = null;
  linkedAddress: boolean = false;
  showContextMenu: boolean = false;
  contextMenuPosition = {x: 0, y: 0};
  selectedBank: any;
  address: any;
  constructor(private addressService: AddressService,
              private userService: UserService,
              private messageService: MessageService,
              private router: Router) {
  }
  ngOnInit(): void {
    this.userService.getPrivateProfile().subscribe(res => {
      this.address = res.data.addresses;
      console.log(this.address)
      if (res.data.addresses.length > 0) {
        this.linkedAddress = true;
      }
    })
    this.addAddressRequest.addressType = 'HOME';
    this.addressService.getListProvince().subscribe(res => {
      if(res.status == 200){
        this.provinces = res.data.items;
      }
    })
  }

  // Khi chọn tỉnh
  onProvinceChange() {
    if (this.selectedProvinceId) {
      this.addressService.getListDistrictByProvince(this.selectedProvinceId).subscribe(data => {
        this.districts = data.data.items;
        this.wards = [];
        this.selectedDistrictId = null;
      });
    } else {
      this.districts = [];
      this.wards = [];
    }
    console.log(this.districts)
  }

  // Khi chọn huyện
  onDistrictChange() {
    if (this.selectedDistrictId) {
      // Lấy danh sách xã
      this.addressService.getListWardsByDistrict(this.selectedProvinceId, this.selectedDistrictId).subscribe(data => {
        this.wards = data.data.items;
      });
    } else {
      this.wards = [];
    }
  }
  addAddress(){
    this.addAddressRequest.provinceId = this.selectedProvinceId;
    this.addAddressRequest.districtId = this.selectedDistrictId;
    this.addAddressRequest.wardId = this.selectedWardId;
    this.userService.addAddress(this.addAddressRequest).subscribe(res =>{
      this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
      setTimeout(() => {
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([this.router.url]);
      }, 700);
    })
  }
  onRightClick(event: MouseEvent, bank: any) {
    event.preventDefault(); // Prevent the default context menu
    this.showContextMenu = true;
    this.contextMenuPosition = {x: event.clientX, y: event.clientY};
    this.selectedBank = bank;
  }

  // Handle delete bank
  deleteAddress(id: any) {
    console.log(id)
    this.addressService.unLinkAddress(id).subscribe(res => {
      this.messageService.add({severity: 'success', summary: 'Thao tác', detail: res.message});
      setTimeout(() => {
        this.router.routeReuseStrategy.shouldReuseRoute = () => false;
        this.router.onSameUrlNavigation = 'reload';
        this.router.navigate([this.router.url]);
      }, 500);
    }, error => {
      this.messageService.add({severity: 'error', summary: 'Thao tác', detail: error.message});
    })
    this.showContextMenu = false; // Hide context menu after action
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    this.showContextMenu = false;
  }
}
