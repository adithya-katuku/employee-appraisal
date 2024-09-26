import AttributeModel from "./AttributeModel";

interface AppraisalModel{
    id:number;
    startDate:Date;
    endDate:Date;
    attributes:AttributeModel[];
    appraisalStatus:string;
    totalRating:number;
}

export default AppraisalModel;