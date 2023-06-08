import { Injectable } from '@angular/core';
// import * as FileSaver from 'file-saver';
// import { Workbook } from 'exceljs';

const EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
const EXCEL_EXTENSION = '.xlsx';
@Injectable({
  providedIn: 'root'
})
export class ExcelService {

  constructor() { }

  public exportExcel(reportHeading: string, reportSubheading: string, headersArray: any[], json: any[], excelFileName: string, sheetName: string) {
    const header = headersArray;
    const data = json;

    // // import('exceljs').then(exceljs => {

    // // })
    // //create workbook and worksheet
    // // const workbook = new Workbook();
    // workbook.creator = 'NTTDATA Morocco';
    // workbook.lastModifiedBy = "NTTDATA Morocco";
    // workbook.created = new Date();
    // workbook.modified = new Date();
    // const worksheet = workbook.addWorksheet(sheetName, {properties:{tabColor:{argb:'FFC0000'}}});

    // // Add header Row
    // worksheet.addRow([]);
    // worksheet.mergeCells('A1:' + this.numToAlpha(header.length - 1) + '1');
    // worksheet.getCell('A1').value = reportHeading;
    // worksheet.getCell('A1').alignment = { horizontal: 'center' };
    // worksheet.getCell('A1').font = { size: 20, bold: true }
    // worksheet.getCell('A1').fill = { type: 'pattern',
    //                                  pattern: 'solid',
    //                                  fgColor: { argb: '6E85B7' },
    //                                  bgColor: { argb: '92B4EC' }}
    
    // if (reportSubheading !== '') {
    //   worksheet.addRow([]);
    //   worksheet.mergeCells('A2:' + this.numToAlpha(header.length - 1) + '2');
    //   worksheet.getCell('A2').value = reportSubheading;
    //   worksheet.getCell('A2').alignment = { horizontal: 'center' };
    //   worksheet.getCell('A2').font = { size: 12, bold: false };
    // }
    

    // worksheet.addRow([]);

    // //Add header Row
    // const headerRow = worksheet.addRow(header);

    // // Cell Style : Fill and Border
    // headerRow.eachCell((cell, index) => {
    //   cell.fill = {
    //     type: 'pattern',
    //     pattern: 'solid',
    //     fgColor: { argb: 'B2C8DF' },
    //     bgColor: { argb: '92B4EC' }
    //   };
    //   cell.border = { top: { style: 'thin' }, left: { style: 'thin' }, bottom: { style: 'thin' }, right: { style: 'thin' } }
    //   cell.font = { size: 12, bold: true }

    //   worksheet.getColumn(index).width = header[index - 1].length < 20 ? 20 : header[index - 1].length;
      
    // });


    // //Get all columns from JSON
    // let columnsArray: any[];
    // for (const key in json) {
    //   if (json.hasOwnProperty(key)) {
    //     columnsArray = Object.keys(json[key]);
    //   }
    // }

    // //Add Data and Conditional Formattings to columns
    // data.forEach((element: any) => {
    //   const eachRow = [];
    //   columnsArray.forEach((column) => {
    //     eachRow.push(element[column]);
    //   });
    //   if (element.isDeleted === "Y") {
    //     const deletedRow = worksheet.addRow(eachRow);
    //     deletedRow.eachCell((cell) => {
    //       cell.font = { name: 'Calibri', family: 4, size: 11, bold: false, strike: true };
    //     });
    //   } else {
    //     worksheet.addRow(eachRow);
    //   }
    // });

    // //Save Excel file 
    // workbook.xlsx.writeBuffer().then((data: ArrayBuffer) => {
    //   const blob = new Blob([data], { type: EXCEL_TYPE });
    //   FileSaver.saveAs(blob, excelFileName, EXCEL_EXTENSION);
    // });
  }

  private numToAlpha(num: number) {
    const alpha = 'A'.charCodeAt(0);
    return String.fromCharCode(alpha + num);
  }
}
