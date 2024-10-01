export default interface NotificationModel{
    notificationId:number;
    notificationTitle:string;
    description:string;
    read:boolean;
    fromId?:number;
}