import React from 'react'

import Bio from '../components/Bio'

const About = props => (
  <div id="content">
    <h1>About</h1>
    <p>
      Hi, my name is Thomas Sojka and this website is about technical part of my
      life. I am working as a Software Engineer at{' '}
      <a href="https://comsysto.com/">Comsysto Reply</a>, where I spend most of
      my working time with Frontend Development in <code>JavaScript</code>.{' '}
      <a href="https://www.gnu.org/software/emacs/">Emacs</a> is my editor of
      choice and I spent a lot of time refining{' '}
      <a href="https://github.com/rollacaster/.emacs.d">my config</a>. Besides{' '}
      <code>JavaScript</code> I have experience with <code>elm</code>,{' '}
      <code>elisp</code> and <code>Java</code>.
    </p>
    <hr />
    <Bio />
    <hr />
    <div>
      <h2>Impressum (Germany)</h2>
      <p>Angaben gemäß § 5 TMG.</p>
      <h3>Kontakt</h3>
      <p>E-Mail: thomas (a) sojka . tech</p>
      <h3>Verantwortlich für den Inhalt</h3>
      <p>
        Nach § 55 Abs. 2 RStV: <br />Thomas Sojka<br /> Rüthlingstraße 6 <br />80636
        München
      </p>
      <h3>Haftung für Inhalte</h3>
      <p>
        Die Inhalte unserer Seiten wurden mit größter Sorgfalt erstellt. Für die
        Richtigkeit, Vollständigkeit und Aktualität der Inhalte können wir
        jedoch keine Gewähr übernehmen. Als Diensteanbieter sind wir gemäß § 7
        Abs.1 TMG für eigene Inhalte auf diesen Seiten nach den allgemeinen
        Gesetzen verantwortlich. Nach §§ 8 bis 10 TMG sind wir als
        Diensteanbieter jedoch nicht verpflichtet, übermittelte oder
        gespeicherte fremde Informationen zu überwachen oder nach Umständen zu
        forschen, die auf eine rechtswidrige Tätigkeit hinweisen.
        Verpflichtungen zur Entfernung oder Sperrung der Nutzung von
        Informationen nach den allgemeinen Gesetzen bleiben hiervon unberührt.
        Eine diesbezügliche Haftung ist jedoch erst ab dem Zeitpunkt der
        Kenntnis einer konkreten Rechtsverletzung möglich. Bei Bekanntwerden von
        entsprechenden Rechtsverletzungen werden wir diese Inhalte umgehend
        entfernen.
      </p>
      <h3>Haftung für Links</h3>
      <p>
        Unser Angebot enthält Links zu externen Webseiten Dritter, auf deren
        Inhalte wir keinen Einfluss haben. Deshalb können wir für diese fremden
        Inhalte auch keine Gewähr übernehmen. Für die Inhalte der verlinkten
        Seiten ist stets der jeweilige Anbieter oder Betreiber der Seiten
        verantwortlich. Die verlinkten Seiten wurden zum Zeitpunkt der
        Verlinkung auf mögliche Rechtsverstöße überprüft. Rechtswidrige Inhalte
        waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente
        inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete
        Anhaltspunkte einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden
        von Rechtsverletzungen werden wir derartige Links umgehend entfernen.
      </p>
      <h3>Urheberrecht</h3>
      <p>
        Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen
        Seiten unterliegen dem deutschen Urheberrecht. Die Vervielfältigung,
        Bearbeitung, Verbreitung und jede Art der Verwertung außerhalb der
        Grenzen des Urheberrechtes bedürfen der schriftlichen Zustimmung des
        jeweiligen Autors bzw. Erstellers. Downloads und Kopien dieser Seite
        sind nur für den privaten, nicht kommerziellen Gebrauch gestattet.
        Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden,
        werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte
        Dritter als solche gekennzeichnet. Sollten Sie trotzdem auf eine
        Urheberrechtsverletzung aufmerksam werden, bitten wir um einen
        entsprechenden Hinweis. Bei Bekanntwerden von Rechtsverletzungen werden
        wir derartige Inhalte umgehend entfernen.
      </p>
      <h3>Datenschutz</h3>
      <p>
        Die Nutzung unserer Webseite ist in der Regel ohne Angabe
        personenbezogener Daten möglich. Soweit auf unseren Seiten
        personenbezogene Daten (beispielsweise Name, Anschrift oder
        eMail-Adressen) erhoben werden, erfolgt dies, soweit möglich, stets auf
        freiwilliger Basis. Diese Daten werden ohne Ihre ausdrückliche
        Zustimmung nicht an Dritte weitergegeben.
      </p>
      <p>
        Wir weisen darauf hin, dass die Datenübertragung im Internet (z.B. bei
        der Kommunikation per E-Mail) Sicherheitslücken aufweisen kann. Ein
        lückenloser Schutz der Daten vor dem Zugriff durch Dritte ist nicht
        möglich.
      </p>
      <p>
        Der Nutzung von im Rahmen der Impressumspflicht veröffentlichten
        Kontaktdaten durch Dritte zur Übersendung von nicht ausdrücklich
        angeforderter Werbung und Informationsmaterialien wird hiermit
        ausdrücklich widersprochen. Die Betreiber der Seiten behalten sich
        ausdrücklich rechtliche Schritte im Falle der unverlangten Zusendung von
        Werbeinformationen, etwa durch Spam-Mails, vor.
      </p>
    </div>
  </div>
)

export default About
