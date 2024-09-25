interface TaskModel {
  taskId?: number;
  taskTitle: string;
  description: string;
  startDate: Date;
  endDate: Date;
  appraisable: boolean;
  selfRating?: number;
  adminRating?: number;
}

export default TaskModel;
