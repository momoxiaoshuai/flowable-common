import{V as Fe,s as m,H as Pe,aJ as Ue,aK as Y,r as M,G as _,O as ye,M as F,W as be,Y as Z,m as Ye,_ as Q,$ as _e,p as ke,b4 as Xe,ad as qe,q as xe,aV as Ge,C as we,o as Je,aP as Ze,b as v,c as S,k as h,F as ee,x as g,g as t,v as j,d as N,j as E,w as R,a4 as W,ae as Ce,e as Qe,y as et,aL as tt,t as U,z as Se,A as ot}from"./index-B-OkF-Vi.js";import{H as te,U as oe,q as at,t as Ie,h as nt,u as st,L as lt,n as it,p as Ee,o as rt,V as ut,au as ct,av as dt,l as pt,j as B,v as ft}from"./_plugin-vue_export-helper--f4TL7q-.js";const vt=()=>Fe&&/firefox/i.test(window.navigator.userAgent),mt=a=>/([\uAC00-\uD7AF\u3130-\u318F])+/gi.test(a),ht=a=>a,gt=["class","style"],yt=/^on[A-Z]/,bt=(a={})=>{const{excludeListeners:u=!1,excludeKeys:s}=a,o=m(()=>((s==null?void 0:s.value)||[]).concat(gt)),i=Pe();return i?m(()=>{var d;return Ue(Object.entries((d=i.proxy)==null?void 0:d.$attrs).filter(([r])=>!o.value.includes(r)&&!(u&&yt.test(r))))}):m(()=>({}))};function xt(a){let u;function s(){if(a.value==null)return;const{selectionStart:i,selectionEnd:d,value:r}=a.value;if(i==null||d==null)return;const l=r.slice(0,Math.max(0,i)),c=r.slice(Math.max(0,d));u={selectionStart:i,selectionEnd:d,value:r,beforeTxt:l,afterTxt:c}}function o(){if(a.value==null||u==null)return;const{value:i}=a.value,{beforeTxt:d,afterTxt:r,selectionStart:l}=u;if(d==null||r==null||l==null)return;let c=i.length;if(i.endsWith(r))c=i.length-r.length;else if(i.startsWith(d))c=d.length;else{const b=d[l-1],f=i.indexOf(b,l-1);f!==-1&&(c=f+1)}a.value.setSelectionRange(c,c)}return[s,o]}function wt(a,{beforeFocus:u,afterFocus:s,beforeBlur:o,afterBlur:i}={}){const d=Pe(),{emit:r}=d,l=Y(),c=M(!1),b=y=>{ye(u)&&u(y)||c.value||(c.value=!0,r("focus",y),s==null||s())},f=y=>{var x;ye(o)&&o(y)||y.relatedTarget&&((x=l.value)!=null&&x.contains(y.relatedTarget))||(c.value=!1,r("blur",y),i==null||i())},w=()=>{var y,x;(y=l.value)!=null&&y.contains(document.activeElement)&&l.value!==document.activeElement||(x=a.value)==null||x.focus()};return _(l,y=>{y&&y.setAttribute("tabindex","-1")}),te(l,"focus",b,!0),te(l,"blur",f,!0),te(l,"click",w,!0),{isFocused:c,wrapperRef:l,handleFocus:b,handleBlur:f}}function Ct({afterComposition:a,emit:u}){const s=M(!1),o=l=>{u==null||u("compositionstart",l),s.value=!0},i=l=>{var c;u==null||u("compositionupdate",l);const b=(c=l.target)==null?void 0:c.value,f=b[b.length-1]||"";s.value=!mt(f)},d=l=>{u==null||u("compositionend",l),s.value&&(s.value=!1,F(()=>a(l)))};return{isComposing:s,handleComposition:l=>{l.type==="compositionend"?d(l):i(l)},handleCompositionStart:o,handleCompositionUpdate:i,handleCompositionEnd:d}}let C;const St=`
  height:0 !important;
  visibility:hidden !important;
  ${vt()?"":"overflow:hidden !important;"}
  position:absolute !important;
  z-index:-1000 !important;
  top:0 !important;
  right:0 !important;
`,It=["letter-spacing","line-height","padding-top","padding-bottom","font-family","font-weight","font-size","text-rendering","text-transform","width","text-indent","padding-left","padding-right","border-width","box-sizing"];function Et(a){const u=window.getComputedStyle(a),s=u.getPropertyValue("box-sizing"),o=Number.parseFloat(u.getPropertyValue("padding-bottom"))+Number.parseFloat(u.getPropertyValue("padding-top")),i=Number.parseFloat(u.getPropertyValue("border-bottom-width"))+Number.parseFloat(u.getPropertyValue("border-top-width"));return{contextStyle:It.map(r=>`${r}:${u.getPropertyValue(r)}`).join(";"),paddingSize:o,borderSize:i,boxSizing:s}}function ze(a,u=1,s){var o;C||(C=document.createElement("textarea"),document.body.appendChild(C));const{paddingSize:i,borderSize:d,boxSizing:r,contextStyle:l}=Et(a);C.setAttribute("style",`${l};${St}`),C.value=a.value||a.placeholder||"";let c=C.scrollHeight;const b={};r==="border-box"?c=c+d:r==="content-box"&&(c=c-i),C.value="";const f=C.scrollHeight-i;if(be(u)){let w=f*u;r==="border-box"&&(w=w+i+d),c=Math.max(w,c),b.minHeight=`${w}px`}if(be(s)){let w=f*s;r==="border-box"&&(w=w+i+d),c=Math.min(w,c)}return b.height=`${c}px`,(o=C.parentNode)==null||o.removeChild(C),C=void 0,b}const zt=Ye({id:{type:String,default:void 0},size:_e,disabled:Boolean,modelValue:{type:Q([String,Number,Object]),default:""},maxlength:{type:[String,Number]},minlength:{type:[String,Number]},type:{type:String,default:"text"},resize:{type:String,values:["none","both","horizontal","vertical"]},autosize:{type:Q([Boolean,Object]),default:!1},autocomplete:{type:String,default:"off"},formatter:{type:Function},parser:{type:Function},placeholder:{type:String},form:{type:String},readonly:Boolean,clearable:Boolean,showPassword:Boolean,showWordLimit:Boolean,suffixIcon:{type:Ie},prefixIcon:{type:Ie},containerRole:{type:String,default:void 0},tabindex:{type:[String,Number],default:0},validateEvent:{type:Boolean,default:!0},inputStyle:{type:Q([Object,Array,String]),default:()=>ht({})},autofocus:Boolean,rows:{type:Number,default:2},...at(["ariaLabel"])}),Ft={[oe]:a=>Z(a),input:a=>Z(a),change:a=>Z(a),focus:a=>a instanceof FocusEvent,blur:a=>a instanceof FocusEvent,clear:()=>!0,mouseleave:a=>a instanceof MouseEvent,mouseenter:a=>a instanceof MouseEvent,keydown:a=>a instanceof Event,compositionstart:a=>a instanceof CompositionEvent,compositionupdate:a=>a instanceof CompositionEvent,compositionend:a=>a instanceof CompositionEvent},Pt=ke({name:"ElInput",inheritAttrs:!1}),kt=ke({...Pt,props:zt,emits:Ft,setup(a,{expose:u,emit:s}){const o=a,i=Xe(),d=bt(),r=qe(),l=m(()=>[o.type==="textarea"?ae.b():n.b(),n.m(y.value),n.is("disabled",x.value),n.is("exceed",Re.value),{[n.b("group")]:r.prepend||r.append,[n.m("prefix")]:r.prefix||o.prefixIcon,[n.m("suffix")]:r.suffix||o.suffixIcon||o.clearable||o.showPassword,[n.bm("suffix","password-clear")]:K.value&&q.value,[n.b("hidden")]:o.type==="hidden"},i.class]),c=m(()=>[n.e("wrapper"),n.is("focus",D.value)]),{form:b,formItem:f}=it(),{inputId:w}=rt(o,{formItemContext:f}),y=st(),x=lt(),n=xe("input"),ae=xe("textarea"),A=Y(),I=Y(),X=M(!1),H=M(!1),ne=M(),O=Y(o.inputStyle),P=m(()=>A.value||I.value),{wrapperRef:Ve,isFocused:D,handleFocus:Ne,handleBlur:Te}=wt(P,{beforeFocus(){return x.value},afterBlur(){var e;o.validateEvent&&((e=f==null?void 0:f.validate)==null||e.call(f,"blur").catch(p=>Ee()))}}),se=m(()=>{var e;return(e=b==null?void 0:b.statusIcon)!=null?e:!1}),T=m(()=>(f==null?void 0:f.validateState)||""),le=m(()=>T.value&&ut[T.value]),$e=m(()=>H.value?ct:dt),Le=m(()=>[i.style]),ie=m(()=>[o.inputStyle,O.value,{resize:o.resize}]),z=m(()=>Ge(o.modelValue)?"":String(o.modelValue)),K=m(()=>o.clearable&&!x.value&&!o.readonly&&!!z.value&&(D.value||X.value)),q=m(()=>o.showPassword&&!x.value&&!!z.value&&(!!z.value||D.value)),k=m(()=>o.showWordLimit&&!!o.maxlength&&(o.type==="text"||o.type==="textarea")&&!x.value&&!o.readonly&&!o.showPassword),G=m(()=>z.value.length),Re=m(()=>!!k.value&&G.value>Number(o.maxlength)),Be=m(()=>!!r.suffix||!!o.suffixIcon||K.value||o.showPassword||k.value||!!T.value&&se.value),[Me,Ae]=xt(A);pt(I,e=>{if(He(),!k.value||o.resize!=="both")return;const p=e[0],{width:V}=p.contentRect;ne.value={right:`calc(100% - ${V+15+6}px)`}});const $=()=>{const{type:e,autosize:p}=o;if(!(!Fe||e!=="textarea"||!I.value))if(p){const V=we(p)?p.minRows:void 0,he=we(p)?p.maxRows:void 0,ge=ze(I.value,V,he);O.value={overflowY:"hidden",...ge},F(()=>{I.value.offsetHeight,O.value=ge})}else O.value={minHeight:ze(I.value).minHeight}},He=(e=>{let p=!1;return()=>{var V;if(p||!o.autosize)return;((V=I.value)==null?void 0:V.offsetParent)===null||(e(),p=!0)}})($),L=()=>{const e=P.value,p=o.formatter?o.formatter(z.value):z.value;!e||e.value===p||(e.value=p)},J=async e=>{Me();let{value:p}=e.target;if(o.formatter&&(p=o.parser?o.parser(p):p),!ue.value){if(p===z.value){L();return}s(oe,p),s("input",p),await F(),L(),Ae()}},re=e=>{s("change",e.target.value)},{isComposing:ue,handleCompositionStart:ce,handleCompositionUpdate:de,handleCompositionEnd:pe}=Ct({emit:s,afterComposition:J}),Oe=()=>{H.value=!H.value,fe()},fe=async()=>{var e;await F(),(e=P.value)==null||e.focus()},De=()=>{var e;return(e=P.value)==null?void 0:e.blur()},Ke=e=>{X.value=!1,s("mouseleave",e)},je=e=>{X.value=!0,s("mouseenter",e)},ve=e=>{s("keydown",e)},We=()=>{var e;(e=P.value)==null||e.select()},me=()=>{s(oe,""),s("change",""),s("clear"),s("input","")};return _(()=>o.modelValue,()=>{var e;F(()=>$()),o.validateEvent&&((e=f==null?void 0:f.validate)==null||e.call(f,"change").catch(p=>Ee()))}),_(z,()=>L()),_(()=>o.type,async()=>{await F(),L(),$()}),Je(()=>{!o.formatter&&o.parser,L(),F($)}),u({input:A,textarea:I,ref:P,textareaStyle:ie,autosize:Ze(o,"autosize"),isComposing:ue,focus:fe,blur:De,select:We,clear:me,resizeTextarea:$}),(e,p)=>(v(),S("div",{class:g([t(l),{[t(n).bm("group","append")]:e.$slots.append,[t(n).bm("group","prepend")]:e.$slots.prepend}]),style:Se(t(Le)),onMouseenter:je,onMouseleave:Ke},[h(" input "),e.type!=="textarea"?(v(),S(ee,{key:0},[h(" prepend slot "),e.$slots.prepend?(v(),S("div",{key:0,class:g(t(n).be("group","prepend"))},[j(e.$slots,"prepend")],2)):h("v-if",!0),N("div",{ref_key:"wrapperRef",ref:Ve,class:g(t(c))},[h(" prefix slot "),e.$slots.prefix||e.prefixIcon?(v(),S("span",{key:0,class:g(t(n).e("prefix"))},[N("span",{class:g(t(n).e("prefix-inner"))},[j(e.$slots,"prefix"),e.prefixIcon?(v(),E(t(B),{key:0,class:g(t(n).e("icon"))},{default:R(()=>[(v(),E(W(e.prefixIcon)))]),_:1},8,["class"])):h("v-if",!0)],2)],2)):h("v-if",!0),N("input",Ce({id:t(w),ref_key:"input",ref:A,class:t(n).e("inner")},t(d),{minlength:e.minlength,maxlength:e.maxlength,type:e.showPassword?H.value?"text":"password":e.type,disabled:t(x),readonly:e.readonly,autocomplete:e.autocomplete,tabindex:e.tabindex,"aria-label":e.ariaLabel,placeholder:e.placeholder,style:e.inputStyle,form:e.form,autofocus:e.autofocus,role:e.containerRole,onCompositionstart:t(ce),onCompositionupdate:t(de),onCompositionend:t(pe),onInput:J,onChange:re,onKeydown:ve}),null,16,["id","minlength","maxlength","type","disabled","readonly","autocomplete","tabindex","aria-label","placeholder","form","autofocus","role","onCompositionstart","onCompositionupdate","onCompositionend"]),h(" suffix slot "),t(Be)?(v(),S("span",{key:1,class:g(t(n).e("suffix"))},[N("span",{class:g(t(n).e("suffix-inner"))},[!t(K)||!t(q)||!t(k)?(v(),S(ee,{key:0},[j(e.$slots,"suffix"),e.suffixIcon?(v(),E(t(B),{key:0,class:g(t(n).e("icon"))},{default:R(()=>[(v(),E(W(e.suffixIcon)))]),_:1},8,["class"])):h("v-if",!0)],64)):h("v-if",!0),t(K)?(v(),E(t(B),{key:1,class:g([t(n).e("icon"),t(n).e("clear")]),onMousedown:et(t(tt),["prevent"]),onClick:me},{default:R(()=>[Qe(t(ft))]),_:1},8,["class","onMousedown"])):h("v-if",!0),t(q)?(v(),E(t(B),{key:2,class:g([t(n).e("icon"),t(n).e("password")]),onClick:Oe},{default:R(()=>[(v(),E(W(t($e))))]),_:1},8,["class"])):h("v-if",!0),t(k)?(v(),S("span",{key:3,class:g(t(n).e("count"))},[N("span",{class:g(t(n).e("count-inner"))},U(t(G))+" / "+U(e.maxlength),3)],2)):h("v-if",!0),t(T)&&t(le)&&t(se)?(v(),E(t(B),{key:4,class:g([t(n).e("icon"),t(n).e("validateIcon"),t(n).is("loading",t(T)==="validating")])},{default:R(()=>[(v(),E(W(t(le))))]),_:1},8,["class"])):h("v-if",!0)],2)],2)):h("v-if",!0)],2),h(" append slot "),e.$slots.append?(v(),S("div",{key:1,class:g(t(n).be("group","append"))},[j(e.$slots,"append")],2)):h("v-if",!0)],64)):(v(),S(ee,{key:1},[h(" textarea "),N("textarea",Ce({id:t(w),ref_key:"textarea",ref:I,class:[t(ae).e("inner"),t(n).is("focus",t(D))]},t(d),{minlength:e.minlength,maxlength:e.maxlength,tabindex:e.tabindex,disabled:t(x),readonly:e.readonly,autocomplete:e.autocomplete,style:t(ie),"aria-label":e.ariaLabel,placeholder:e.placeholder,form:e.form,autofocus:e.autofocus,rows:e.rows,role:e.containerRole,onCompositionstart:t(ce),onCompositionupdate:t(de),onCompositionend:t(pe),onInput:J,onFocus:t(Ne),onBlur:t(Te),onChange:re,onKeydown:ve}),null,16,["id","minlength","maxlength","tabindex","disabled","readonly","autocomplete","aria-label","placeholder","form","autofocus","rows","role","onCompositionstart","onCompositionupdate","onCompositionend","onFocus","onBlur"]),t(k)?(v(),S("span",{key:0,style:Se(ne.value),class:g(t(n).e("count"))},U(t(G))+" / "+U(e.maxlength),7)):h("v-if",!0)],64))],38))}});var Vt=nt(kt,[["__file","input.vue"]]);const Lt=ot(Vt);export{Lt as E,wt as a,ht as m,Ct as u};
