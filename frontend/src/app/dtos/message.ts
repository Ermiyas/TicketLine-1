export class Message {
  constructor(
    public id: number,
    public title: string,
    public summary: string,
    public text: string,
    public image: File,
    public publishedAt: string) {
  }
}
