// import jsPDF, { CellConfig } from 'jspdf';
import { Session } from "@models/Session.model";
import { Candidate } from "@models/Candidate.model";
import { Injectable } from "@angular/core";
import { Internship } from "@models/internship.model";
import jsPDF, { CellConfig } from "jspdf";
import { EInternshipType } from "@models/InternshipType.enum";

@Injectable({
  providedIn: "root",
})
export class PdfService {
  constructor() {}

  public exportPDFCertificate(body: Internship): File {
    let pdf = new jsPDF("p", "mm", "a4");
    const pdfWidth = pdf.internal.pageSize.getWidth();

    pdf.addImage(
      "../../../../assets/img/nttdata.png",
      "png",
      pdfWidth - 18 - 60,
      15,
      60,
      20
    );

    pdf.setFontSize(14);

    pdf.setFont("times", "bold");

    const title = "ATTESTATION DE STAGE";

    const titleWidth = pdf.getTextWidth(title);
    const textWidth = pdfWidth - 19 * 2;

    const startText = (pdfWidth - textWidth) / 2;
    const startTitle = 74;

    pdf.text(title, startTitle, 60);
    pdf.setLineWidth(0.5);
    pdf.line(startTitle, 61, startTitle + titleWidth + 2.5, 61);

    pdf.setFontSize(12);
    pdf.setFont("times", "normal");

    let part1 =
      "Nous soussignés, NTT DATA Morocco Centers SARL, Société à responsabilité limitée au capital social d’un montant de 7.700.000,00 DH, attestons par la présente que M.  ";

    const part1Lines = pdf.splitTextToSize(part1, textWidth);
    const lastPart1LineWidth =
      (pdf.getStringUnitWidth(part1Lines.slice(-1)[0]) * 12) /
      pdf.internal.scaleFactor;

    pdf.setFont("times", "bold");
    const boldText =
      "\n".repeat(part1Lines.length - 1) +
      " ".repeat(lastPart1LineWidth / pdf.getTextWidth(" ")) +
      body.candidate.firstName +
      " " +
      body.candidate.lastName +
      ",";
    pdf.setFont("times", "normal");

    const boldTextLines = pdf.splitTextToSize(part1 + boldText, textWidth);
    const lastBoldTexLineWidth =
      (pdf.getStringUnitWidth(boldTextLines.slice(-1)[0]) * 12) /
      pdf.internal.scaleFactor;

    const part2 =
      "\n".repeat(boldTextLines.length - part1Lines.length) +
      " ".repeat(lastBoldTexLineWidth / pdf.getTextWidth(" ")) +
      "   étudiant à " +
      body.candidate.university +
      ", a effectué un stage" +
      (body.type == EInternshipType.TYPE_PFE
        ? " de projet de fin d'études"
        : body.type == EInternshipType.TYPE_PFA
        ? " de projet de fin d'année"
        : "") +
      " dans notre entreprise, durant la période du " +
      new Date(body.startDate).toLocaleDateString("fr-FR", {
        day: "numeric",
        month: "long",
        year: "numeric",
      }) +
      " au " +
      new Date(body.endDate).toLocaleDateString("fr-FR", {
        day: "numeric",
        month: "long",
        year: "numeric",
      }) +
      ".";

    pdf.text(part1, startText, 85, {
      maxWidth: textWidth,
      align: "justify",
    });

    pdf.setFont("times", "bold");
    pdf.text(boldText, startText, 85, {
      maxWidth: textWidth,
      align: "justify",
    });

    pdf.setFont("times", "normal");
    pdf.text(part2, startText, 85, {
      maxWidth: textWidth,
      align: "justify",
    });

    pdf.text(
      "Par son enthousiasme, créativité, rigueur... et ses qualités professionnelles et humaines, " +
        body.candidate.firstName +
        " " +
        body.candidate.lastName +
        " a rempli tous les objectifs de son stage. Sa présence et ses réalisations ont été satisfaisantes à tous points de vue.",
      startText,
      110,
      { maxWidth: textWidth, align: "justify" }
    );
    pdf.text("Pour servir et valoir ce que de droit.", startText, 140);
    pdf.text(
      "Fait à Tétouan, le " +
        new Date().toLocaleDateString("fr-FR", {
          weekday: "short",
          month: "short",
          day: "numeric",
          year: "numeric",
        }),
      startText,
      150
    );
    pdf.text("Fréderic SABBAH", startText, 165);
    pdf.text("Directeur", startText, 170);

    pdf.setLineWidth(0.5);
    pdf.line(18.3 + 173, 272, 18.3, 272);
    pdf.setFontSize(11);

    pdf.text(
      "NTT DATA Morocco Centers – SARL au capital de 7.700.000 Dhs – Parc Technologique de Tétouan shore,\nRoute de Cabo Negro, Martil – Maroc – RC : 19687 – IF : 15294847 – CNSS : 4639532 –\nTaxe Prof. :51840121 – ICE 000253700000046",
      105,
      276,
      { maxWidth: 170, align: "center" }
    );

    pdf.save('certificate_'+body.internshipId+'.pdf');

    const pdfContent = pdf.output("blob");
    const pdfFile = new File(
      [pdfContent],
      "certificate_" + body.internshipId + ".pdf",
      { type: "application/pdf" }
    );
    
    return pdfFile;
  }

  public exportPDF(
    header: string[],
    body: Candidate[],
    session: Session,
    isWaiting: boolean,
    isInterview: boolean
  ) {
    let date = new Date().toDateString();
    let pdf = new jsPDF("p", "mm", "a4");
    // set Header
    pdf.addImage("../../../../assets/img/nttdata.png", "png", 5, 1, 35, 20);
    pdf.setFontSize(10);
    pdf.setFont("times", "normal");
    pdf.text("Date : " + date, 203, 10, undefined, "right");
    // set Title of pdf
    pdf.setFontSize(28);
    pdf.setFont("times", "bold");
    if (isWaiting) {
      pdf.text("List of waited candidates", 105, 40, undefined, "center");
    } else if (isInterview) {
      pdf.text(
        "Candidates accepted in the technical test",
        105,
        40,
        undefined,
        "center"
      );
    } else {
      pdf.text("List of admitted candidates", 105, 40, undefined, "center");
    }
    // set info additionnal of pdf
    pdf.setFontSize(12);
    pdf.setFont("times", "normal");
    pdf.text("Session : " + session.sessionName, 7, 55, undefined);
    pdf.text("Technology : " + session.technology, 200, 55, undefined, "right");
    if (isWaiting == true) {
      pdf.text(
        "Candidates in the waiting list : " + session.admittedNumber,
        7,
        62,
        undefined
      );
    } else if (isWaiting == false) {
      pdf.text(
        "Candidates Admitted : " + session.admittedNumber,
        7,
        62,
        undefined
      );
    }
    // Add table
    if (isInterview) {
      pdf.table(
        38,
        67,
        this.getDataForPdfTable(body, isInterview),
        this.createHeadersForPdfTable(header, isInterview),
        { autoSize: false, headerBackgroundColor: "#87CEEB" }
      );
    } else {
      pdf.table(
        7,
        67,
        this.getDataForPdfTable(body, isInterview),
        this.createHeadersForPdfTable(header, isInterview),
        { autoSize: false, headerBackgroundColor: "#87CEEB" }
      );
    }
    pdf.save("angular-demo.pdf");
  }

  private createHeadersForPdfTable(keys: string[], isInterview: boolean) {
    const result: CellConfig[] = [];
    let width = 0;
    for (let i = 0; i < keys.length; i += 1) {
      if (keys[i] === "Email") {
        width = 90;
      } else if (
        isInterview &&
        (keys[i] === "First name" || keys[i] === "Last name")
      ) {
        width = 90;
      } else {
        width = 57;
      }
      result.push({
        name: keys[i],
        prompt: keys[i],
        width: width,
        align: "center",
        padding: 0,
      });
    }
    return result;
  }

  private getDataForPdfTable(body: Candidate[], isInterview: boolean) {
    const data = [];
    for (let i = 0; i < body.length; i++) {
      if (isInterview) {
        data.push({
          "First name": body[i].firstName,
          "Last name": body[i].lastName,
        });
      } else {
        data.push({
          "First name": body[i].firstName,
          "Last name": body[i].lastName,
          Email: body[i].email,
          "Phone number": body[i].phoneNumber,
        });
      }
    }
    return data;
  }
}
